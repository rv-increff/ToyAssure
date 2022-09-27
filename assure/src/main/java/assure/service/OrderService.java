package assure.service;

import assure.dao.OrderDao;
import assure.dao.OrderItemDao;
import assure.pojo.OrderItemPojo;
import assure.pojo.OrderPojo;
import assure.spring.ApiException;
import commons.util.InvoiceType;
import commons.util.OrderStatus;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static commons.util.OrderStatus.ALLOCATED;
import static commons.util.OrderStatus.FULFILLED;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderService {

    private static final Map<OrderStatus, OrderStatus> validStatusUpdateMap =
            ImmutableMap.<OrderStatus, OrderStatus>builder()
                    .put(OrderStatus.CREATED, ALLOCATED)
                    .put(ALLOCATED, FULFILLED)
                    .build();
    //TODO DEV_REVIEW add access specifiers
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;

    public void add(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojoList) throws ApiException {
        checkDuplicateGlobalSkuIdNotExits(orderItemPojoList);

        orderPojo.setStatus(OrderStatus.CREATED);
        orderDao.add(orderPojo);
        Long orderId = orderPojo.getId();

        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            add(orderItemPojo, orderId);
        }
    }

    public List<OrderPojo> selectOrder(Integer pageNumber, Integer pageSize) {
        return orderDao.select(pageNumber, pageSize);
    }

    public List<OrderPojo> selectOrderByInvoiceType(Integer pageNumber, Integer pageSize, InvoiceType type) {
        return orderDao.selectOrderByInvoiceType(pageNumber, pageSize, type);
    }

    public List<OrderItemPojo> selectOrderItem(Long orderId) {
        return orderItemDao.selectByOrderId(orderId);
    }

    public OrderPojo selectByChannelIdAndChannelOrderId(Long channelId, String channelOrderId) {
        return orderDao.selectByChannelIdAndChannelOrderId(channelId, channelOrderId);
    }

    public OrderPojo getCheck(Long id) throws ApiException {
        OrderPojo orderPojo = orderDao.selectById(id);
        if (isNull(orderPojo)) {
            throw new ApiException("order does not exist");
        }
        return orderPojo;
    }

    public Long increaseAllocateQtyForOrderItem(OrderItemPojo orderItemPojo, Long qtyToIncrease) throws ApiException {
        if (qtyToIncrease < 0)
            throw new ApiException("Quantity to increase should be greater than 0");

        orderItemPojo.setAllocatedQuantity(orderItemPojo.getAllocatedQuantity() + qtyToIncrease);
        return orderItemPojo.getAllocatedQuantity();
    }

    public void checkOrderStatusValid(OrderStatus initialStatus, OrderStatus finalStatus) throws ApiException {
        if (validStatusUpdateMap.get(initialStatus) != finalStatus) {
            throw new ApiException("Invalid order update status");
        }
    }

    public void markStatusAllocated(Long id) throws ApiException {
        OrderPojo orderPojo = getCheck(id);
        checkOrderStatusValid(orderPojo.getStatus(), ALLOCATED);

        List<OrderItemPojo> orderItemPojoList = selectOrderItem(id);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            if (!Objects.equals(orderItemPojo.getOrderedQuantity(), orderItemPojo.getAllocatedQuantity()))
                return;
        }
        updateStatus(id, ALLOCATED);
    }

    public void markStatusFulfilled(Long id) throws ApiException {
        OrderPojo orderPojo = getCheck(id);
        checkOrderStatusValid(orderPojo.getStatus(), FULFILLED);

        List<OrderItemPojo> orderItemPojoList = selectOrderItem(id);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            if (!Objects.equals(orderItemPojo.getOrderedQuantity(), orderItemPojo.getFulfilledQuantity()))
                throw new ApiException("Could not fulfill order");
        }
        updateStatus(id, FULFILLED);
    }

    public OrderStatus updateStatus(Long id, OrderStatus orderStatus) throws ApiException {
        OrderPojo orderPojo = getCheck(id);
        orderPojo.setStatus(orderStatus);
        orderDao.update();
        return orderStatus;
    }

    public Long fulfillQty(OrderItemPojo orderItemPojo) {
        orderItemPojo.setFulfilledQuantity(orderItemPojo.getAllocatedQuantity());
        orderItemPojo.setAllocatedQuantity(0L);
        orderDao.update();

        return orderItemPojo.getFulfilledQuantity();
    }

    public void updateUrl(Long id, String url) throws ApiException {
        OrderPojo orderPojo = getCheck(id);
        orderPojo.setInvoiceUrl(url);
        orderDao.update();
    }

    private void add(OrderItemPojo orderItemPojo, Long orderId) {
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setFulfilledQuantity(0L);
        orderItemPojo.setAllocatedQuantity(0L);
        orderItemDao.add(orderItemPojo);
    }

    private void checkDuplicateGlobalSkuIdNotExits(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        Set<Long> globalSkuIdSet = orderItemPojoList.stream().map(OrderItemPojo::getGlobalSkuId).collect(Collectors.toSet());
        if (globalSkuIdSet.size() != orderItemPojoList.size())
            throw new ApiException("Duplicate global SKU ID present in list");
    }
}
