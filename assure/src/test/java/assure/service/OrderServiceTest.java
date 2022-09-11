package assure.service;

import assure.config.QaConfig;
import assure.util.AbstractTest;
import assure.pojo.OrderItemPojo;
import assure.pojo.OrderPojo;
import assure.spring.ApiException;
import assure.util.OrderStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static assure.util.RandomUtil.*;
import static java.lang.Math.min;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class OrderServiceTest extends AbstractTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void addTest() throws ApiException {
        OrderPojo orderPojo = getOrder();
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            orderItemPojoList.add(orderItemAdd(orderPojo.getId()));
        }
        orderService.add(orderPojo, orderItemPojoList);
    }

    @Test
    public void selectOrderTest() {
        List<OrderPojo> orderPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            orderPojoList.add(orderAdd());
        }
        Assert.assertEquals(new HashSet<>(orderPojoList), new HashSet<>(orderService.selectOrder(0, 5)));
    }

    @Test
    public void selectOrderItemTest() {
        OrderPojo orderPojo = orderAdd();
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            orderItemPojoList.add(orderItemAdd(orderPojo.getId()));
        }
        Assert.assertEquals(new HashSet<>(orderItemPojoList),new HashSet<>(orderService.selectOrderItem(orderPojo.getId())));
    }

    @Test
    public void selectByChannelIdAndChannelOrderIdTest() {
        OrderPojo orderPojo = orderAdd();
        OrderPojo actualPojo = orderService.selectByChannelIdAndChannelOrderId(orderPojo.getChannelId(),
                orderPojo.getChannelOrderId());
        Assert.assertEquals(orderPojo, actualPojo);

    }

    @Test
    public void allocateOrderItemQtyTest() {
        OrderItemPojo orderItemPojo = orderItemAdd(getRandomNumberLong());
        Long invQty = getRandomNumberLong();
        Long orderedQty = orderItemPojo.getOrderedQuantity();
        Long allocatedQtyPrev = orderItemPojo.getAllocatedQuantity();
        Long allocatedQty = orderService.allocateOrderItemQty(orderItemPojo, invQty);
        Assert.assertEquals((long) allocatedQty, min(invQty, orderedQty - allocatedQtyPrev));
        Assert.assertEquals(allocatedQty, orderItemPojo.getAllocatedQuantity());
    }

    @Test
    public void updateStatusTest() throws ApiException {
        int ind = getRandomNumber() % 3;
        OrderStatus orderStatus = OrderStatus.CREATED;
        switch (ind) {
            case 1:
                orderStatus = OrderStatus.ALLOCATED;
            case 2:
                orderStatus = OrderStatus.FULFILLED;
        }
        OrderPojo orderPojo = orderAdd();
        orderService.updateStatus(orderPojo.getId(), orderStatus);
        Assert.assertEquals(orderStatus, orderSelect().get(0).getStatus());
    }

    @Test
    public void getCheckTest() throws ApiException {
        try {
            orderService.getCheck(getRandomNumberLong());
            fail("error should be thrown");
        } catch (ApiException e) {
            Assert.assertEquals("order does not exist", e.getMessage());
        }

        OrderPojo orderPojo = orderAdd();
        Assert.assertEquals(orderService.getCheck(orderPojo.getId()), orderPojo);
    }

    @Test
    public void fulfillQtyTest() {
        OrderItemPojo orderItemPojo = orderItemAdd(getRandomNumberLong());
        Long allocatedQty = orderItemPojo.getAllocatedQuantity();
        orderService.fulfillQty(orderItemPojo);
        Assert.assertEquals(allocatedQty, orderItemPojo.getFulfilledQuantity());
        Assert.assertEquals(0L, (long) orderItemPojo.getAllocatedQuantity());
    }

    @Test
    public void setUrl() throws ApiException {
        OrderPojo orderPojo = orderAdd();
        String url = getRandomString();
        orderService.updateUrl(orderPojo.getId(), url);
        assertEquals(url, orderPojo.getInvoiceUrl());
    }

}
