package assure.dto;

import assure.model.ErrorData;
import assure.model.OrderForm;
import assure.model.OrderItemForm;
import assure.pojo.*;
import assure.service.*;
import assure.spring.ApiException;
import assure.util.OrderStatus;
import assure.util.PartyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static assure.util.ConversionUtil.*;
import static assure.util.ValidationUtil.throwErrorIfNotEmpty;
import static assure.util.ValidationUtil.validateList;
import static java.util.Objects.isNull;

@Service
public class OrderDto {
    private static final Long MAX_LIST_SIZE = 1000L;
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

        Map<String, Long> clientSkuIdToGlobalSkuIdMap = getCheckClientSkuId(orderItemFormList);
        OrderPojo orderPojo = convertOrderFormToOrderPojo(orderForm);
        List<OrderItemPojo> orderItemPojoList = convertOrderFormToOrderItemPojo(orderForm.getOrderItemFormList(),
                clientSkuIdToGlobalSkuIdMap);
        orderService.add(orderPojo, orderItemPojoList);

        return orderItemFormList.size();
    }
    @Transactional(rollbackFor = ApiException.class)
    public Long allocateOrder(Long id) throws ApiException {
        OrderPojo orderPojo = orderService.getCheck(id);

        if(!(orderPojo.getStatus()== OrderStatus.CREATED)){
            throw new ApiException("cannot allocate order status must be CREATED");
        }

        List<OrderItemPojo> orderItemPojoList = orderService.selectOrderItemListByOrderId(orderPojo.getId());
        Map<OrderItemPojo,InventoryPojo>orderItemPojoInvQtyMap = getOrderItemPojoInvQtyMap(orderItemPojoList);
        Long countOfFullyAllocatedOrderItems = 0L;
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            Long invQty = orderItemPojoInvQtyMap.get(orderItemPojo).getAvailableQuantity();
            Long allocatedQty = orderService.allocateOrderItemQty(orderItemPojo,invQty);
            inventoryService.allocateQty(allocatedQty,orderItemPojo.getGlobalSkuId());
            binSkuService.allocateQty(allocatedQty,orderItemPojo.getGlobalSkuId());

            if(orderItemPojo.getOrderedQuantity() == orderItemPojo.getAllocatedQuantity())
                countOfFullyAllocatedOrderItems++;
        }
        if(countOfFullyAllocatedOrderItems == orderItemPojoList.size())
            orderService.updateStatusToAllocated(id);

        return id;
    }

    private Map<OrderItemPojo, InventoryPojo> getOrderItemPojoInvQtyMap(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        Map<OrderItemPojo,InventoryPojo>orderItemPojoInvQtyMap = new HashMap<>();
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            InventoryPojo inventoryPojo = inventoryService.selectByGlobalSkuId(orderItemPojo.getGlobalSkuId());
            if(isNull(inventoryPojo)){
                errorFormList.add(new ErrorData(row, "inventory for orderItem does not exists"));
            }else {
                orderItemPojoInvQtyMap.put(orderItemPojo,inventoryPojo);
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
        return orderItemPojoInvQtyMap;
    }

    private Map<String, Long> getCheckClientSkuId(List<OrderItemForm> orderItemFormList) throws ApiException {
        Map<String, Long> clientSkuIdToGlobalSkuIdMap = new HashMap<>();

        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (OrderItemForm orderItemForm : orderItemFormList) {
            ProductPojo productPojo = productService.selectByClientSkuId(orderItemForm.getClientSkuId());
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