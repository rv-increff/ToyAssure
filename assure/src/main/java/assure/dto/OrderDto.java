package assure.dto;

import assure.model.OrderForm;
import assure.model.*;
import assure.pojo.*;
import assure.service.*;
import assure.spring.ApiException;
import assure.util.ConversionUtil;
import commons.util.InvoiceType;
import commons.util.PartyType;
import commons.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static assure.util.ConversionUtil.*;
import static assure.util.DataUtil.getKey;
import static assure.util.DataUtil.returnFileStream;
import static commons.util.OrderStatus.*;
import static assure.util.ValidationUtil.validateForm;
import static assure.util.ValidationUtil.validateList;
import static commons.util.pdfUtil.convertToPDF;
import static commons.util.pdfUtil.jaxbObjectToXML;
import static java.lang.Math.min;
import static java.util.Objects.isNull;

@Service
public class OrderDto {
    private static final Long MAX_LIST_SIZE = 1000L;
    private static final Integer PAGE_SIZE = 5;
    private static final String INTERNAL_CHANNEL = "INTERNAL";


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
    @Autowired
    private ChannelClient channelClient;

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

        List<String> clientSkuIdList = orderItemFormList.stream().map(OrderItemForm::getClientSkuId).collect(Collectors.toList());
        Map<String, Long> clientSkuIdToGlobalSkuIdMap = productService.getCheckClientSkuId(clientSkuIdList, orderForm.getClientId());

        OrderPojo orderPojo = convertOrderFormToOrderPojo(orderForm, channelId);
        List<OrderItemPojo> orderItemPojoList = convertOrderItemListToOrderItemPojo(orderForm.getOrderItemFormList(),
                clientSkuIdToGlobalSkuIdMap);
        orderService.add(orderPojo, orderItemPojoList);

        return orderItemFormList.size();
    }

    //TODO DEV_REVIEW:rename to ChannelOrderForm done
    @Transactional(rollbackFor = ApiException.class)
    public Integer addChannelOrder(ChannelOrderForm channelOrderForm) throws ApiException {

        List<OrderItemFormChannel> orderItemFormChannelList = channelOrderForm.getOrderItemFormChannelList();
        validateForm(channelOrderForm);
        validateList("order Item List", orderItemFormChannelList, MAX_LIST_SIZE);
        checkDuplicateChannelSkuIds(orderItemFormChannelList);

        partyService.checkByIdAndType(channelOrderForm.getClientId(), PartyType.CLIENT);
        partyService.checkByIdAndType(channelOrderForm.getCustomerId(), PartyType.CUSTOMER);

        Long channelId = channelService.getCheck(channelOrderForm.getChannelId()).getId();

        String channelOrderId = channelOrderForm.getChannelOrderId();
        checkChannelIdAndChannelOrderIdPairNotExist(channelId, channelOrderId);

        Map<String, Long> channelSkuIdToGlobalSkuIdMap = channelListingService.getCheckChannelSkuId(orderItemFormChannelList,
                channelOrderForm.getClientId(), channelOrderForm.getChannelId());
        OrderPojo orderPojo = convertOrderFormChannelToOrderPojo(channelOrderForm, channelId);
        List<OrderItemPojo> orderItemPojoList = convertOrderItemListChannelToOrderItemPojo(
                channelOrderForm.getOrderItemFormChannelList(),
                channelSkuIdToGlobalSkuIdMap);

        orderService.add(orderPojo, orderItemPojoList);

        return orderItemFormChannelList.size();
    }
    @Transactional(rollbackFor = ApiException.class)
    public OrderData allocateOrder(Long id) throws ApiException {
        OrderPojo orderPojo = orderService.getCheck(id);
        // move this to API done

        List<OrderItemPojo> orderItemPojoList = orderService.selectOrderItem(orderPojo.getId());
        List<InventoryPojo> inventoryPojoList = inventoryService.getAllForGskus(orderItemPojoList.stream().
                map(OrderItemPojo::getGlobalSkuId).collect(Collectors.toList()));
        Map<Long, InventoryPojo> gskuToInv = InventoryService.getGskuToInventory(inventoryPojoList);

        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            Long invQty = gskuToInv.get(orderItemPojo.getGlobalSkuId()).getAvailableQuantity();
            Long qtyToAllocateMore = min(invQty, orderItemPojo.getOrderedQuantity() - orderItemPojo.getAllocatedQuantity());

            Long allocatedQty = orderService.increaseAllocateQtyForOrderItem(orderItemPojo, qtyToAllocateMore);

            inventoryService.allocateQty(qtyToAllocateMore, orderItemPojo.getGlobalSkuId());
            binSkuService.allocateQty(allocatedQty, orderItemPojo.getGlobalSkuId());
        }

        orderService.markStatusAllocated(id);
        return convertOrderPojoToData(orderPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderData fulfillOrder(Long id) throws ApiException {
        OrderPojo orderPojo = orderService.getCheck(id);


        List<OrderItemPojo> orderItemPojoList = orderService.selectOrderItem(orderPojo.getId());
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            Long fulfilledQty = orderService.fulfillQty(orderItemPojo);
            inventoryService.fulfillQty(fulfilledQty, orderItemPojo.getGlobalSkuId());
        }
        //check for fulfill === ordered
        //same for allocated
        //markOrderAsFulfilled -> checkOrderStatus valid and item wise
        orderService.markStatusFulfilled(id);
        return convertOrderPojoToData(orderPojo);
    }

    public List<OrderData> selectOrder(Integer pageNumber) throws ApiException {
        return convertOrderPojoListToData(orderService.selectOrder(pageNumber, PAGE_SIZE));
    }

    public List<OrderData> selectOrderItemsByInvoiceType(Integer pageNumber, InvoiceType type) throws ApiException {
        return convertOrderPojoListToData(orderService.selectOrderByInvoiceType(pageNumber, PAGE_SIZE, type));
    }

    public List<OrderItemData> selectOrderItems(Long orderId) {
        return convertOrderItemPojoListToData(orderService.selectOrderItem(orderId));
    }

    public byte[] getInvoice(Long orderId) throws Exception {
        OrderPojo orderPojo = orderService.getCheck(orderId);
        if (orderPojo.getStatus() != FULFILLED)
            throw new ApiException("Order should be fulfilled for invoice generation");

        if (!isNull(orderPojo.getInvoiceUrl())) {
            return returnFileStream(orderPojo.getInvoiceUrl());
        }
        String url = getInvoiceUrl(orderPojo);
        orderService.updateUrl(orderId, url);
        return returnFileStream(url);
    }
    public List<OrderItemData> convertOrderItemPojoListToData(List<OrderItemPojo> orderItemPojoList) {
        List<OrderItemData> orderItemInvoiceDataList = new ArrayList<>();
        Map<Long, ProductPojo> globalSkuIdToPojo = productService.getGlobalSkuIdToPojo(orderItemPojoList.stream().
                map(OrderItemPojo::getGlobalSkuId).collect(Collectors.toSet()));

        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            orderItemInvoiceDataList.add(convertOrderItemPojToData(orderItemPojo,
                    globalSkuIdToPojo.getOrDefault(orderItemPojo.getGlobalSkuId(), new ProductPojo()).getClientSkuId()));
        }
        return orderItemInvoiceDataList;
    }
    private String getInvoiceUrl(OrderPojo orderPojo) throws Exception {
        String url = null;
        Long orderId = orderPojo.getId();
        if (channelService.getCheck(orderPojo.getChannelId()).getInvoiceType() == InvoiceType.SELF) {
            url = createPdfAndGetUrl(orderId);
        } else {
            byte[] encoded = fetchInvoiceFromChannel(orderId);
            byte[] pdfByteArray = java.util.Base64.getDecoder().decode(encoded);

            String pdfName = orderId + "_invoice.pdf";
            File pdfFile = new File("src/invoice", pdfName);
            OutputStream out = Files.newOutputStream(pdfFile.getAbsoluteFile().toPath());
            out.write(pdfByteArray);
            out.close();
            url = pdfFile.getAbsolutePath();
        }
        return url;
    }

    private String createPdfAndGetUrl(Long orderId) throws ApiException, IOException, TransformerException {
        OrderPojo orderPojo = orderService.getCheck(orderId);
        List<OrderItemPojo> orderItemPojoList = orderService.selectOrderItem(orderId);
        List<OrderItemInvoiceData> orderItemInvoiceDataList = new ArrayList<>();

        Map<Long, ProductPojo> gskuToProductPojo = productService.getGlobalSkuIdToPojo(orderItemPojoList.stream().
                map(OrderItemPojo::getGlobalSkuId).collect(Collectors.toSet()));
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            String clientSkuId = gskuToProductPojo.get(orderItemPojo.getGlobalSkuId()).getClientSkuId();
            OrderItemInvoiceData orderItemInvoiceData = convertPojoOrderItemToData(orderItemPojo, clientSkuId, orderPojo.getChannelOrderId());
            orderItemInvoiceDataList.add(orderItemInvoiceData);
        }

        ZonedDateTime time = ZonedDateTime.now();
        Double total = 0.;

        for (OrderItemInvoiceData i : orderItemInvoiceDataList) {
            total += i.getOrderedQuantity() * i.getSellingPricePerUnit();
        }
        InvoiceData oItem = new InvoiceData(time, orderPojo.getChannelOrderId(), orderItemInvoiceDataList, total);

        String xml = jaxbObjectToXML(oItem, InvoiceData.class);
        String pdfName = orderId + "_invoice.pdf";
        File xsltFile = new File("src/invoiceTemplate", "invoice.xsl");
        File pdfFile = new File("src/invoice", pdfName);
        System.out.println(xml);
        convertToPDF(oItem, xsltFile, pdfFile, xml);
        String url = pdfFile.toPath().toAbsolutePath().toString();
        return url;
    }

    private void checkChannelIdAndChannelOrderIdPairNotExist(Long channelId, String channelOrderId) throws ApiException {
        if (!isNull(orderService.selectByChannelIdAndChannelOrderId(channelId, channelOrderId))) {
            throw new ApiException("channel order id exists for the channel");
        }
    }

    private byte[] fetchInvoiceFromChannel(Long orderId) throws Exception {
        OrderPojo orderPojo = orderService.getCheck(orderId);
        List<OrderItemPojo> orderItemPojoList = orderService.selectOrderItem(orderId);
        List<OrderItemChannelData> orderItemChannelDataList = new ArrayList<>();

        List<Long> gSkuList = orderItemPojoList.stream().map(OrderItemPojo::getGlobalSkuId).collect(Collectors.toList());
        Map<String, ChannelListingPojo> globalSkuIdAndChannelIdAndClientIdToPojo = channelListingService.
                getGlobalSkuIdAndChannelIdAndClientIdToPojo(gSkuList, orderPojo.getChannelId(), orderPojo.getClientId());

        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            String key = getKey(Arrays.asList(orderItemPojo.getGlobalSkuId(),orderPojo.getChannelId(), orderPojo.getClientId()));
            String channelSkuId = globalSkuIdAndChannelIdAndClientIdToPojo.get(key).getChannelSkuId();
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

        return channelClient.post("orders/get-invoice", invoiceData).getBytes();
    }

    private List<OrderData> convertOrderPojoListToData(List<OrderPojo> orderPojoList) throws ApiException {
        if (CollectionUtils.isEmpty(orderPojoList))
            return new ArrayList<>();
        List<OrderData> orderDataList = new ArrayList<>();

        Map<Long, ChannelPojo> channelIdToPojo = channelService.getCheckChannelIdToPojo(orderPojoList.stream().
                map(OrderPojo::getChannelId).distinct().collect(Collectors.toList()));
        List<Long> partyIds = orderPojoList.stream().map(OrderPojo::getClientId).distinct().collect(Collectors.toList());
        partyIds.addAll(orderPojoList.stream().map(OrderPojo::getCustomerId).distinct().collect(Collectors.toList()));
        Map<Long, PartyPojo> partyIdToPojo = partyService.getCheckPartyIdToPojo(partyIds);

        for (OrderPojo orderPojo : orderPojoList) {

            String channelName = channelIdToPojo.get(orderPojo.getChannelId()).getName();
            String clientName = partyIdToPojo.get(orderPojo.getClientId()).getName();
            String customerName = partyIdToPojo.get(orderPojo.getCustomerId()).getName();

            orderDataList.add(ConversionUtil.convertOrderPojoToData(orderPojo, channelName, clientName, customerName));
        }
        return orderDataList;
    }
    private OrderData convertOrderPojoToData(OrderPojo orderPojo) throws ApiException {
        if (isNull(orderPojo))
            return new OrderData();
        String channelName = channelService.getCheck(orderPojo.getChannelId()).getName();
        String clientName = partyService.getCheck(orderPojo.getClientId()).getName();
        String customerName = partyService.getCheck(orderPojo.getClientId()).getName();
        OrderData orderData = new OrderData();
        orderData.setChannelOrderId(orderPojo.getChannelOrderId());
        orderData.setInvoiceUrl(orderPojo.getInvoiceUrl());
        orderData.setChannelName(channelName);
        orderData.setClientName(clientName);
        orderData.setCustomerName(customerName);
        orderData.setStatus(orderPojo.getStatus());
        orderData.setId(orderPojo.getId());

        return orderData;
    }
}