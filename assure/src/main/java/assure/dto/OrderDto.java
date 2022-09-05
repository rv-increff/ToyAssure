package assure.dto;

import assure.model.*;
import assure.model.OrderForm;
import assure.pojo.*;
import assure.service.*;
import assure.spring.ApiException;
import assure.util.InvoiceType;
import assure.util.OrderStatus;
import assure.util.PartyType;
import com.google.common.collect.ImmutableMap;
import commons.model.*;
import commons.requests.Requests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static assure.util.ConversionUtil.*;
import static assure.util.DataUtil.returnFileStream;
import static assure.util.OrderStatus.ALLOCATED;
import static assure.util.OrderStatus.FULFILLED;
import static assure.util.ValidationUtil.*;
import static commons.requests.Requests.objectToJsonString;
import static commons.util.pdfUtil.convertToPDF;
import static commons.util.pdfUtil.jaxbObjectToXML;
import static java.util.Objects.isNull;

@Service
public class OrderDto {
    private static final Long MAX_LIST_SIZE = 1000L;
    private static final Integer PAGE_SIZE = 10;
    private static final String INTERNAL_CHANNEL = "INTERNAL";

    private static final Map<OrderStatus, OrderStatus> validStatusUpdateMap =
            ImmutableMap.<OrderStatus, OrderStatus>builder()
                    .put(OrderStatus.CREATED, ALLOCATED)
                    .put(ALLOCATED, FULFILLED)
                    .build();


    @Autowired
    private OrderService orderService;
    @Autowired
    private PartyService partyService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BinSkuService binSkuService;
    @Autowired
    private ChannelListingService channelListingService;

    @Transactional(rollbackFor = ApiException.class)
    public Integer add(OrderForm orderForm) throws ApiException {
        List<OrderItemForm> orderItemFormList = orderForm.getOrderItemFormList();
        validateForm(orderForm);
        validateList("order Item List", orderItemFormList, MAX_LIST_SIZE);
        checkDuplicateClientSkuIds(orderItemFormList);

        partyService.checkByIdAndType(orderForm.getClientId(), PartyType.CLIENT);
        partyService.checkByIdAndType(orderForm.getCustomerId(), PartyType.CUSTOMER);

        Long channelId = channelService.selectByName(INTERNAL_CHANNEL).getId();

        String channelOrderId = orderForm.getChannelOrderId();
        checkChannelIdAndChannelOrderIdPairNotExist(channelId, channelOrderId);

        Map<String, Long> clientSkuIdToGlobalSkuIdMap = getCheckClientSkuId(orderItemFormList, orderForm.getClientId());
        OrderPojo orderPojo = convertOrderFormToOrderPojo(orderForm, channelId);
        List<OrderItemPojo> orderItemPojoList = convertOrderItemListToOrderItemPojo(orderForm.getOrderItemFormList(),
                clientSkuIdToGlobalSkuIdMap);
        orderService.add(orderPojo, orderItemPojoList);

        return orderItemFormList.size();
    }

    //TODO DEV_REVIEW:rename to ChannelOrderForm
    @Transactional(rollbackFor = ApiException.class)
    public Integer addChannelOrder(OrderFormChannel orderFormChannel) throws ApiException {

        List<OrderItemFormChannel> orderItemFormChannelList = orderFormChannel.getOrderItemFormChannelList();
        validateForm(orderFormChannel);
        validateList("order Item List", orderItemFormChannelList, MAX_LIST_SIZE);
        checkDuplicateChannelSkuIds(orderItemFormChannelList);

        partyService.checkByIdAndType(orderFormChannel.getClientId(), PartyType.CLIENT);
        partyService.checkByIdAndType(orderFormChannel.getCustomerId(), PartyType.CUSTOMER);

        Long channelId = channelService.getCheck(orderFormChannel.getChannelId()).getId();

        String channelOrderId = orderFormChannel.getChannelOrderId();
        checkChannelIdAndChannelOrderIdPairNotExist(channelId, channelOrderId);

        Map<String, Long> channelSkuIdToGlobalSkuIdMap = getCheckChannelSkuId(orderItemFormChannelList,
                orderFormChannel.getClientId(), orderFormChannel.getChannelId());
        OrderPojo orderPojo = convertOrderFormChannelToOrderPojo(orderFormChannel, channelId);
        List<OrderItemPojo> orderItemPojoList = convertOrderItemListChannelToOrderItemPojo(
                orderFormChannel.getOrderItemFormChannelList(),
                channelSkuIdToGlobalSkuIdMap);

        orderService.add(orderPojo, orderItemPojoList);

        return orderItemFormChannelList.size();
    }

    //TODO DEV_REVIEW:not the correct way of doing this. Two different API's should be there. allocateOrder and FulfillOrder.
    @Transactional(rollbackFor = ApiException.class)
    public OrderStatusUpdateForm updateStatus(OrderStatusUpdateForm orderStatusUpdateForm) throws ApiException {
        validateForm(orderStatusUpdateForm);
        OrderPojo orderPojo = orderService.getCheck(orderStatusUpdateForm.getOrderId());

        //TODO DEV_REVIEW:Invalid and not "invalid"
        if (validStatusUpdateMap.get(orderPojo.getStatus()) != orderStatusUpdateForm.getUpdateStatusTo()) {
            throw new ApiException("Invalid order update status");
        }
        OrderStatus orderStatus = validStatusUpdateMap.get(orderPojo.getStatus());
        switch (orderStatus) {

            case ALLOCATED:
                allocateOrder(orderStatusUpdateForm.getOrderId());
                return orderStatusUpdateForm;

            case FULFILLED:
                fulfillOrder(orderStatusUpdateForm.getOrderId());
                return orderStatusUpdateForm;
        }

        return orderStatusUpdateForm;
    }

    public List<OrderData> selectOrder(Integer pageNumber){
        return convertOrderPojoListToData(orderService.selectOrder(pageNumber,PAGE_SIZE));
    }
    public List<OrderItemData> selectOrderItems(Long orderId){
        return convertOrderItemPojoListToData(orderService.selectOrderItem(orderId));
    }

    public byte[] getInvoice(Long orderId) throws Exception {
        OrderPojo orderPojo = orderService.getCheck(orderId);
        if (orderPojo.getStatus() != FULFILLED)
            throw new ApiException("order should be fulfilled for invoice generation");

        if (!isNull(orderPojo.getInvoiceUrl())) {
            return returnFileStream(orderPojo.getInvoiceUrl());
        }
        String url = null;

        if (channelService.getCheck(orderPojo.getChannelId()).getInvoiceType() == InvoiceType.SELF) {
            url = createPdfAndGetUrl(orderId);
        } else {
            byte[] encoded = callChannelAndGetPdfByteArray(orderId);
            byte[] pdfByteArray = java.util.Base64.getDecoder().decode(encoded);

            String pdfName = orderId + "_invoice.pdf";
            File pdfFile = new File("src", pdfName);
            OutputStream out = Files.newOutputStream(pdfFile.getAbsoluteFile().toPath());
            out.write(pdfByteArray);
            out.close();
            url = pdfFile.getAbsolutePath();
        }
        orderService.setUrl(orderId, url);
        return returnFileStream(url);
    }

    @Transactional(rollbackFor = ApiException.class)
    private void allocateOrder(Long id) throws ApiException {
        OrderPojo orderPojo = orderService.getCheck(id);

        List<OrderItemPojo> orderItemPojoList = orderService.selectOrderItem(orderPojo.getId());
        Map<OrderItemPojo, InventoryPojo> orderItemPojoInvQtyMap = getOrderItemPojoInvQtyMap(orderItemPojoList);
        Long countOfFullyAllocatedOrderItems = 0L;
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            Long invQty = orderItemPojoInvQtyMap.get(orderItemPojo).getAvailableQuantity();
            Long allocatedQty = orderService.allocateOrderItemQty(orderItemPojo, invQty);
            inventoryService.allocateQty(allocatedQty, orderItemPojo.getGlobalSkuId());
            binSkuService.allocateQty(allocatedQty, orderItemPojo.getGlobalSkuId());

            if (orderItemPojo.getOrderedQuantity() == orderItemPojo.getAllocatedQuantity())
                countOfFullyAllocatedOrderItems++;
        }
        if (countOfFullyAllocatedOrderItems == orderItemPojoList.size())
            orderService.updateStatus(id, ALLOCATED);

    }

    //TODO DEV_REVIEW: order should move to fulfilled on invoice genration
    @Transactional(rollbackFor = ApiException.class)
    private void fulfillOrder(Long id) throws ApiException {
        OrderPojo orderPojo = orderService.getCheck(id);
        List<OrderItemPojo> orderItemPojoList = orderService.selectOrderItem(orderPojo.getId());
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            Long fulfilledQty = orderService.fulfillQty(orderItemPojo);
            inventoryService.fulfillQty(fulfilledQty, orderItemPojo.getGlobalSkuId());

        }
        orderService.updateStatus(id, FULFILLED);
    }

    private String createPdfAndGetUrl(Long orderId) throws ApiException, IOException, TransformerException {
        OrderPojo orderPojo = orderService.getCheck(orderId);
        List<OrderItemPojo> orderItemPojoList = orderService.selectOrderItem(orderId); //TODO remove invoice controller
        List<OrderItemInvoiceData> orderItemInvoiceDataList = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            String clientSkuId = productService.selectByGlobalSkuId(orderItemPojo.getGlobalSkuId()).getClientSkuId();
            OrderItemInvoiceData orderItemInvoiceData = convertPojoOrderItemToData(orderItemPojo, clientSkuId, orderPojo.getChannelOrderId());
            orderItemInvoiceDataList.add(orderItemInvoiceData);
        }

        ZonedDateTime time = orderPojo.getCreatedAt();
        Double total = 0.;

        for (OrderItemInvoiceData i : orderItemInvoiceDataList) {
            total += i.getOrderedQuantity() * i.getSellingPricePerUnit();
        }
        InvoiceData oItem = new InvoiceData(time, orderPojo.getChannelOrderId(), orderItemInvoiceDataList, total);

        String xml = jaxbObjectToXML(oItem, InvoiceData.class);
        String pdfName = orderId + "_invoice.pdf";
        File xsltFile = new File("src", "invoice.xsl");
        File pdfFile = new File("src", pdfName);
        System.out.println(xml);
        convertToPDF(oItem, xsltFile, pdfFile, xml);
        String url = pdfFile.toPath().toAbsolutePath().toString();
        return url;
    }

    private Map<OrderItemPojo, InventoryPojo> getOrderItemPojoInvQtyMap(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        Map<OrderItemPojo, InventoryPojo> orderItemPojoInvQtyMap = new HashMap<>();
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            InventoryPojo inventoryPojo = inventoryService.selectByGlobalSkuId(orderItemPojo.getGlobalSkuId());
            if (isNull(inventoryPojo)) {
                errorFormList.add(new ErrorData(row, "inventory for orderItem does not exists"));
            } else {
                orderItemPojoInvQtyMap.put(orderItemPojo, inventoryPojo);
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
        return orderItemPojoInvQtyMap;
    }

    //TODO DEV_REVIEW:this could be handled by static method in ProductService.
    private Map<String, Long> getCheckClientSkuId(List<OrderItemForm> orderItemFormList, Long clientId) throws ApiException {
        Map<String, Long> clientSkuIdToGlobalSkuIdMap = new HashMap<>();

        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (OrderItemForm orderItemForm : orderItemFormList) {
            ProductPojo productPojo = productService.selectByClientSkuIdAndClientId(orderItemForm.getClientSkuId(), clientId);
            if (isNull(productPojo)) {
                errorFormList.add(new ErrorData(row, "clientSkuID does not exists"));
                continue;
            }
            clientSkuIdToGlobalSkuIdMap.put(orderItemForm.getClientSkuId(), productPojo.getGlobalSkuId());

            row++;
        }
        throwErrorIfNotEmpty(errorFormList);

        return clientSkuIdToGlobalSkuIdMap;
    }

    private Map<String, Long> getCheckChannelSkuId(List<OrderItemFormChannel> orderItemFormChannelList, Long clientId,
                                                   Long channelId) throws ApiException {
        Map<String, Long> channelSkuIdToGlobalSkuIdMap = new HashMap<>();

        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (OrderItemFormChannel orderItemFormChannel : orderItemFormChannelList) {
            ChannelListingPojo channelListingPojo = channelListingService.selectByChannelIdAndClientIdAndChannelSkuId(
                    channelId,clientId, orderItemFormChannel.getChannelSkuId());
            if (isNull(channelListingPojo)) {
                errorFormList.add(new ErrorData(row, "channelSkuID does not exists"));
                continue;
            }
            channelSkuIdToGlobalSkuIdMap.put(orderItemFormChannel.getChannelSkuId(), channelListingPojo.getGlobalSkuId());

            row++;
        }
        throwErrorIfNotEmpty(errorFormList);

        return channelSkuIdToGlobalSkuIdMap;
    }


    private void checkChannelIdAndChannelOrderIdPairNotExist(Long channelId, String channelOrderId) throws ApiException {
        if (!isNull(orderService.selectByChannelIdAndChannelOrderId(channelId, channelOrderId))) {
            throw new ApiException("channel order id exists for the channel");
        }
    }

    //TODO DEV_REVIEW:rename method to fetchInvoiceFromChannel

    private byte[] callChannelAndGetPdfByteArray(Long orderId) throws Exception {
        OrderPojo orderPojo = orderService.getCheck(orderId);
        List<OrderItemPojo> orderItemPojoList = orderService.selectOrderItem(orderId);
        List<OrderItemChannelData> orderItemChannelDataList = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            String channelSkuId = channelListingService.selectByGlobalSkuIdAndChannelIdAndClientId(orderItemPojo.getGlobalSkuId(),
                    orderPojo.getChannelId(), orderPojo.getClientId()).getChannelSkuId();
            OrderItemChannelData orderItemChannelData = convertPojoOrderItemChannelToData(orderItemPojo, channelSkuId,
                    orderPojo.getChannelOrderId());
            orderItemChannelDataList.add(orderItemChannelData);
        }

        ZonedDateTime time = orderPojo.getCreatedAt();
        Double total = 0.;

        for (OrderItemChannelData i : orderItemChannelDataList) {
            total += i.getOrderedQuantity() * i.getSellingPricePerUnit();
        }
        InvoiceDataChannel invoiceData = new InvoiceDataChannel(time, orderPojo.getChannelOrderId(), orderItemChannelDataList, total);


        //TODO DEV_REVIEW:this URL should not be hardcoded. It should be taken from properties file. This code should be in ChannelClient
        //TODO DEV_REVIEW:public methods declared below private.
        //TODO DEV_REVIEW:Correct method name, avoid calling product service agian ans again
        return Requests.post("http://localhost:9001/channel/orders/get-invoice", objectToJsonString(invoiceData)).getBytes();
    }


    public OrderItemData convertOrderItemPojToData(OrderItemPojo orderItemPojo){
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setOrderId(orderItemPojo.getOrderId());
        orderItemData.setId(orderItemPojo.getId());
        orderItemData.setSellingPricePerUnit(orderItemPojo.getSellingPricePerUnit());
        orderItemData.setOrderedQuantity(orderItemPojo.getOrderedQuantity());
        orderItemData.setAllocatedQuantity(orderItemPojo.getAllocatedQuantity());
        orderItemData.setFulfilledQuantity(orderItemPojo.getFulfilledQuantity());
        orderItemData.setClientSkuId(productService.selectByGlobalSkuId(orderItemPojo.getGlobalSkuId()).getClientSkuId());
        return orderItemData;
    }
    public List<OrderItemData> convertOrderItemPojoListToData(List<OrderItemPojo> orderItemPojoList){
        List<OrderItemData> orderItemInvoiceDataList = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            orderItemInvoiceDataList.add(convertOrderItemPojToData(orderItemPojo));
        }
        return orderItemInvoiceDataList;
    }
}