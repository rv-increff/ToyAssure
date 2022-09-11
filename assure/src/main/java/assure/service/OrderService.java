package assure.service;

import assure.dao.OrderDao;
import assure.dao.OrderItemDao;
import assure.pojo.OrderItemPojo;
import assure.pojo.OrderPojo;
import assure.spring.ApiException;
import assure.util.InvoiceType;
import assure.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.Math.min;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderService {

    //TODO DEV_REVIEW add access specifiers
    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderItemDao orderItemDao;

    public void add(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojoList) throws ApiException {
        orderPojo.setStatus(OrderStatus.CREATED);
        orderDao.add(orderPojo);
        Long orderId = orderPojo.getId();


        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            add(orderItemPojo,orderId);
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

    public Long allocateOrderItemQty(OrderItemPojo orderItemPojo, Long invQty) {
        Long allocatedQty = min(invQty, orderItemPojo.getOrderedQuantity() - orderItemPojo.getAllocatedQuantity());
        orderItemPojo.setAllocatedQuantity(allocatedQty);
        return allocatedQty;
    }

    public void updateStatus(Long id, OrderStatus orderStatus) throws ApiException {
        OrderPojo orderPojo = getCheck(id);
        orderPojo.setStatus(orderStatus);
        orderDao.update();
    }

    public Long fulfillQty(OrderItemPojo orderItemPojo) {
        orderItemPojo.setFulfilledQuantity(orderItemPojo.getAllocatedQuantity());
        orderItemPojo.setAllocatedQuantity(0L);
        orderDao.update();

        return orderItemPojo.getFulfilledQuantity();
    }

    //TODO DEV_REVIEW rename it to update url
    public void updateUrl(Long id, String url) throws ApiException {
        OrderPojo orderPojo = getCheck(id);
        orderPojo.setInvoiceUrl(url);
        orderDao.update();
    }
    //TODO DEV_REVIEW make this a separate private method
    private void add(OrderItemPojo orderItemPojo, Long orderId){
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setFulfilledQuantity(0L);
        orderItemPojo.setAllocatedQuantity(0L);
        orderItemDao.add(orderItemPojo);
    }
}
