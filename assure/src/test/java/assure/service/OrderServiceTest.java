package assure.service;

import assure.config.BaseTest;
import assure.util.TestData;
import assure.pojo.OrderItemPojo;
import assure.pojo.OrderPojo;
import assure.spring.ApiException;
import assure.util.RandomUtil;
import commons.util.OrderStatus;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.lang.Math.min;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class OrderServiceTest extends BaseTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private TestData testData;

    @Test
    public void addTest() throws ApiException {
        OrderPojo orderPojo = testData.getOrder();
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            orderItemPojoList.add(testData.orderItemAdd(orderPojo.getId()));
        }
        orderService.add(orderPojo, orderItemPojoList);
    }

    @Test
    public void selectOrderTest() {
        List<OrderPojo> orderPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            orderPojoList.add(testData.orderAdd());
        }
        Assert.assertEquals(new HashSet<>(orderPojoList), new HashSet<>(orderService.selectOrder(0, 5)));
    }

    @Test
    public void selectOrderItemTest() {
        OrderPojo orderPojo = testData.orderAdd();
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            orderItemPojoList.add(testData.orderItemAdd(orderPojo.getId()));
        }
        Assert.assertEquals(new HashSet<>(orderItemPojoList),new HashSet<>(orderService.selectOrderItem(orderPojo.getId())));
    }

    @Test
    public void selectByChannelIdAndChannelOrderIdTest() {
        OrderPojo orderPojo = testData.orderAdd();
        OrderPojo actualPojo = orderService.selectByChannelIdAndChannelOrderId(orderPojo.getChannelId(),
                orderPojo.getChannelOrderId());
        Assert.assertEquals(orderPojo, actualPojo);

    }


    @Test
    public void updateStatusTest() throws ApiException {
        int ind = RandomUtil.getRandomNumber() % 3;
        OrderStatus orderStatus = OrderStatus.CREATED;
        switch (ind) {
            case 1:
                orderStatus = OrderStatus.ALLOCATED;
            case 2:
                orderStatus = OrderStatus.FULFILLED;
        }
        OrderPojo orderPojo = testData.orderAdd();
        orderService.updateStatus(orderPojo.getId(), orderStatus);
        Assert.assertEquals(orderStatus, testData.orderSelect().get(0).getStatus());
    }

    @Test
    public void getCheckTest() throws ApiException {
        try {
            orderService.getCheck(RandomUtil.getRandomNumberLong());
            fail("error should be thrown");
        } catch (ApiException e) {
            Assert.assertEquals("order does not exist", e.getMessage());
        }

        OrderPojo orderPojo = testData.orderAdd();
        Assert.assertEquals(orderService.getCheck(orderPojo.getId()), orderPojo);
    }

    @Test
    public void fulfillQtyTest() {
        OrderItemPojo orderItemPojo = testData.orderItemAdd(RandomUtil.getRandomNumberLong());
        Long allocatedQty = orderItemPojo.getAllocatedQuantity();
        orderService.fulfillQty(orderItemPojo);
        Assert.assertEquals(allocatedQty, orderItemPojo.getFulfilledQuantity());
        Assert.assertEquals(0L, (long) orderItemPojo.getAllocatedQuantity());
    }

    @Test
    public void setUrl() throws ApiException {
        OrderPojo orderPojo = testData.orderAdd();
        String url = RandomUtil.getRandomString();
        orderService.updateUrl(orderPojo.getId(), url);
        assertEquals(url, orderPojo.getInvoiceUrl());
    }

}
