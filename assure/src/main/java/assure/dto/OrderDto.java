package assure.dto;

import assure.model.*;
import assure.pojo.*;
import assure.service.*;
import assure.spring.ApiException;
import assure.util.DataUtil;
import assure.util.OrderStatus;
import assure.util.PartyType;
import com.google.common.collect.ImmutableMap;
import commons.model.ErrorData;
import commons.model.OrderItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

import static assure.util.ConversionUtil.*;
import static assure.util.DataUtil.jaxbObjectToXML;
import static assure.util.OrderStatus.ALLOCATED;
import static assure.util.OrderStatus.FULFILLED;
import static assure.util.ValidationUtil.*;
import static java.util.Objects.isNull;

@Service
public class OrderDto {
    private static final Long MAX_LIST_SIZE = 1000L;
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

    @Transactional(rollbackFor = ApiException.class)//TODO remove if not nessecesary
    public Integer add(OrderForm orderForm) throws ApiException {
        List<OrderItemForm> orderItemFormList = orderForm.getOrderItemFormList();
        validateList("order Item List", orderItemFormList, MAX_LIST_SIZE);
        checkDuplicateClientSkuIds(orderItemFormList);

        partyService.checkByIdAndType(orderForm.getClientId(), PartyType.CLIENT);
        partyService.checkByIdAndType(orderForm.getCustomerId(), PartyType.CUSTOMER);

        ChannelPojo channelPojo = channelService.selectByName(INTERNAL_CHANNEL);
        if (isNull(channelPojo)) {
            throw new ApiException(INTERNAL_CHANNEL + " channel does not exists");
        }

        Long channelId = channelPojo.getId();
        String channelOrderId = orderForm.getChannelOrderId();
        checkChannelIdAndChannelOrderIdPairNotExist(channelId, channelOrderId);

        Map<String, Long> clientSkuIdToGlobalSkuIdMap = getCheckClientSkuId(orderItemFormList,orderForm.getClientId());
        OrderPojo orderPojo = convertOrderFormToOrderPojo(orderForm);
        List<OrderItemPojo> orderItemPojoList = convertOrderFormToOrderItemPojo(orderForm.getOrderItemFormList(),
                clientSkuIdToGlobalSkuIdMap);
        orderService.add(orderPojo, orderItemPojoList);

        return orderItemFormList.size();
    }
    @Transactional(rollbackFor = ApiException.class)
    public OrderStatusUpdateForm updateStatus(OrderStatusUpdateForm orderStatusUpdateForm) throws ApiException {
        validateForm(orderStatusUpdateForm);
        OrderPojo orderPojo = orderService.getCheck(orderStatusUpdateForm.getOrderId());

        if (validStatusUpdateMap.get(orderPojo.getStatus()) != orderStatusUpdateForm.getUpdateStatusTo()) {
            throw new ApiException("invalid order update status");
        }
        OrderStatus orderStatus = validStatusUpdateMap.get(orderPojo.getStatus());
        switch(orderStatus){

            case ALLOCATED:
                allocateOrder(orderStatusUpdateForm.getOrderId());
                return orderStatusUpdateForm;

            case FULFILLED:
                fulfillOrder(orderStatusUpdateForm.getOrderId());
                return orderStatusUpdateForm;
        }

        return orderStatusUpdateForm;
    }

    public String getInvoice(Long orderId) throws ApiException, IOException, TransformerException {
        OrderPojo orderPojo = orderService.getCheck(orderId);
        if(orderPojo.getStatus() != FULFILLED)throw new ApiException("order should be fulfilled for invoice generation");

        if(!isNull(orderPojo.getInvoiceUrl())){
            return orderPojo.getInvoiceUrl();
        }
        String url = null;
        Long internalChannelId = channelService.selectByName(INTERNAL_CHANNEL).getId();
        if(orderPojo.getChannelId().equals(internalChannelId)){
            url = createPdfAndGetUrl(orderId);
        }else{
            //get invoice from channel

        }
        orderService.setUrl(orderId,url);
    return url;
    }

    @Transactional(rollbackFor = ApiException.class)
    private void allocateOrder(Long id) throws ApiException {
        OrderPojo orderPojo = orderService.getCheck(id);

        List<OrderItemPojo> orderItemPojoList = orderService.selectOrderItemListByOrderId(orderPojo.getId());
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

    @Transactional(rollbackFor = ApiException.class)
    private void fulfillOrder(Long id) throws ApiException {
        OrderPojo orderPojo = orderService.getCheck(id);
        List<OrderItemPojo> orderItemPojoList = orderService.selectOrderItemListByOrderId(orderPojo.getId());
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            Long fulfilledQty = orderService.fulfillQty(orderItemPojo);
            inventoryService.fulfillQty(fulfilledQty,orderItemPojo.getGlobalSkuId());

        }
        orderService.updateStatus(id, FULFILLED);
    }

    private String createPdfAndGetUrl(Long orderId) throws ApiException, IOException, TransformerException {
        OrderPojo orderPojo = orderService.getCheck(orderId);
        List<OrderItemPojo> orderItemPojoList = orderService.selectOrderItemByOrderId(orderId); //TODO remove invoice controller
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            String clientSkuId = productService.selectByGlobalSkuId(orderItemPojo.getGlobalSkuId()).getClientSkuId();
            OrderItemData orderItemData = convertPojoOrderItemToData(orderItemPojo,clientSkuId);
            orderItemDataList.add(orderItemData);
        }

        ZonedDateTime time = orderPojo.getCreatedAt();
        Double total = 0.;

        for (OrderItemData i : orderItemDataList) {
            total += i.getOrderedQuantity() * i.getSellingPricePerUnit();
        }
        InvoiceData oItem = new InvoiceData(time, orderId, orderItemDataList, total);


        String xml = jaxbObjectToXML(oItem);
        File xsltFile = new File("src", "invoice.xsl");
        File pdfFile = new File("src", "invoice.pdf");
        System.out.println(xml);
        DataUtil.convertToPDF(oItem, xsltFile, pdfFile, xml);
        return null;
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

    private Map<String, Long> getCheckClientSkuId(List<OrderItemForm> orderItemFormList,Long clientId) throws ApiException {
        Map<String, Long> clientSkuIdToGlobalSkuIdMap = new HashMap<>();

        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (OrderItemForm orderItemForm : orderItemFormList) {
            ProductPojo productPojo = productService.selectByClientSkuIdAndClientId(orderItemForm.getClientSkuId(),clientId);
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


    private void checkChannelIdAndChannelOrderIdPairNotExist(Long channelId, String channelOrderId) throws ApiException {
        if (!isNull(orderService.selectByChannelIdAndChannelOrderId(channelId, channelOrderId))) {
            throw new ApiException("channel order id exists for the channel");
        }
    }
}