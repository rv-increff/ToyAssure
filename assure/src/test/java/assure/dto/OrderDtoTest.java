package assure.dto;

import assure.model.OrderForm;
import assure.model.OrderStatusUpdateForm;
import assure.pojo.OrderPojo;
import assure.pojo.ProductPojo;
import assure.spring.ApiException;
import assure.util.BaseTest;
import assure.util.OrderStatus;
import assure.util.PartyType;
import assure.util.TestData;
import commons.model.OrderItemForm;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static assure.util.RandomUtil.getRandomNumberLong;
import static assure.util.RandomUtil.getRandomString;
import static org.junit.Assert.fail;


public class OrderDtoTest extends BaseTest {

    @Autowired
    private ChannelListingDto channelListingDto;
    @Autowired
    private OrderDto orderDto;
    @Autowired
    private TestData testData;

    @Test(expected = ApiException.class)
    public void addDuplicateClientSkuIdTest() throws ApiException {
        String clientSkuId = getRandomString();
        OrderForm orderForm = new OrderForm();
        orderForm.setChannelOrderId(getRandomString()); //TODO change to private method
        orderForm.setClientId(getRandomNumberLong());
        orderForm.setCustomerId(getRandomNumberLong());
        List<OrderItemForm> orderItemFormList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            OrderItemForm orderItemForm = testData.getOrderItemForm();
            orderItemForm.setClientSkuId(clientSkuId);
            orderItemFormList.add(orderItemForm);
        }
        orderForm.setOrderItemFormList(orderItemFormList);
        orderDto.add(orderForm);
    }

    @Test
    public void addChannelIdAndChannelOrderIdPairErrorTest() throws ApiException {
        OrderForm orderForm = new OrderForm();
        String channelOrderId = getRandomString();
        orderForm.setChannelOrderId(channelOrderId);
        Long clientId = testData.partyAdd(getRandomString(), PartyType.CLIENT).getId();
        ProductPojo productPojo = testData.productAdd(clientId);
        String clientSkuId = productPojo.getClientSkuId();
        Long customerId = testData.partyAdd(getRandomString(), PartyType.CUSTOMER).getId();

        orderForm.setClientId(clientId);
        orderForm.setCustomerId(customerId);
        List<OrderItemForm> orderItemFormList = new ArrayList<>();


        OrderItemForm orderItemForm = testData.getOrderItemForm();
        orderItemForm.setClientSkuId(clientSkuId);
        orderItemFormList.add(orderItemForm);

        orderForm.setOrderItemFormList(orderItemFormList);
        orderDto.add(orderForm);
        try {
            orderDto.add(orderForm);
            fail("error should have occurred");
        } catch (ApiException e) {
            Assert.assertEquals("channel order id exists for the channel", e.getMessage());
        }
    }

    @Test
    public void updateStatusErrorTest() {
        OrderPojo orderPojo = testData.orderAdd();
        orderPojo.setStatus(OrderStatus.CREATED);
        OrderStatusUpdateForm orderStatusUpdateForm = new OrderStatusUpdateForm();
        orderStatusUpdateForm.setOrderId(orderPojo.getId());
        orderStatusUpdateForm.setUpdateStatusTo(OrderStatus.CREATED);
        try {
            orderDto.updateStatus(orderStatusUpdateForm);
            fail("error should have occurred");
        } catch (ApiException e) {
            Assert.assertEquals("Invalid order update status", e.getMessage());
        }
    }

    @Test
    public void updateStatusTest() throws ApiException {
        OrderPojo orderPojo = testData.orderAdd();
        orderPojo.setStatus(OrderStatus.CREATED);
        OrderStatusUpdateForm orderStatusUpdateForm = new OrderStatusUpdateForm();
        orderStatusUpdateForm.setOrderId(orderPojo.getId());
        orderStatusUpdateForm.setUpdateStatusTo(OrderStatus.ALLOCATED);
        orderDto.updateStatus(orderStatusUpdateForm);

    }

    @Test
    public void updateStatusAllocatedTest() throws ApiException {
        OrderPojo orderPojo = testData.orderAdd();
        orderPojo.setStatus(OrderStatus.ALLOCATED);
        OrderStatusUpdateForm orderStatusUpdateForm = new OrderStatusUpdateForm();
        orderStatusUpdateForm.setOrderId(orderPojo.getId());
        orderStatusUpdateForm.setUpdateStatusTo(OrderStatus.FULFILLED);
        orderDto.updateStatus(orderStatusUpdateForm);

    }

}
