package assure.dto;

import assure.config.BaseTest;
import assure.pojo.BinPojo;
import assure.pojo.OrderItemPojo;
import assure.pojo.OrderPojo;
import assure.pojo.ProductPojo;
import assure.spring.ApiException;
import assure.util.RandomUtil;
import assure.util.TestData;
import commons.model.OrderForm;
import commons.model.OrderItemForm;
import commons.model.OrderItemFormChannel;
import commons.util.OrderStatus;
import commons.util.PartyType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static assure.util.RandomUtil.*;
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
        orderForm.setChannelOrderId(getRandomString());
        orderForm.setClientId(RandomUtil.getRandomNumberLong());
        orderForm.setCustomerId(RandomUtil.getRandomNumberLong());
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
    public void addTest() throws ApiException {

        String channelOrderId = getRandomString();

        Long clientId = testData.partyAdd(getRandomString(), PartyType.CLIENT).getId();
        ProductPojo productPojo = testData.productAdd(clientId);
        String clientSkuId = productPojo.getClientSkuId();
        Long customerId = testData.partyAdd(getRandomString(), PartyType.CUSTOMER).getId();

        List<OrderItemForm> orderItemFormList = new ArrayList<>();

        OrderItemForm orderItemForm = testData.getOrderItemForm();
        orderItemForm.setClientSkuId(clientSkuId);
        orderItemFormList.add(orderItemForm);

        orderDto.add(testData.getOrderForm(channelOrderId, clientId, customerId, orderItemFormList));
    }

    @Test
    public void addChannelIdAndChannelOrderIdPairErrorTest() throws ApiException {
        String channelOrderId = getRandomString();

        Long clientId = testData.partyAdd(getRandomString(), PartyType.CLIENT).getId();
        ProductPojo productPojo = testData.productAdd(clientId);
        String clientSkuId = productPojo.getClientSkuId();
        Long customerId = testData.partyAdd(getRandomString(), PartyType.CUSTOMER).getId();

        List<OrderItemForm> orderItemFormList = new ArrayList<>();


        OrderItemForm orderItemForm = testData.getOrderItemForm();
        orderItemForm.setClientSkuId(clientSkuId);
        orderItemFormList.add(orderItemForm);
        OrderForm orderForm = testData.getOrderForm(channelOrderId, clientId, customerId, orderItemFormList);
        orderDto.add(orderForm);
        try {
            orderDto.add(orderForm);
            fail("error should have occurred");
        } catch (ApiException e) {
            Assert.assertEquals("channel order id exists for the channel", e.getMessage());
        }
    }

    @Test(expected = ApiException.class)
    public void addChannelOrderDuplicateChannelSkuIdErrorTest() throws ApiException {

        List<OrderItemFormChannel> orderItemFormChannelList = new ArrayList<>();

        Long clientId = testData.partyAdd(getRandomString(), PartyType.CLIENT).getId();
        Long customerId = testData.partyAdd(getRandomString(), PartyType.CUSTOMER).getId();
        Long channelId = testData.channelAdd().getId();
        String channelSkuId = getRandomString();
        for (int i = 0; i < 5; i++) {
            orderItemFormChannelList.add(testData.getOrderItemFormChannel(channelSkuId, getRandomNumberLong(),
                    getRandomNumberDouble()));
        }
        orderDto.addChannelOrder(testData.getChannelOrderForm(channelId, clientId, customerId, getRandomString(),
                orderItemFormChannelList));
    }

    @Test(expected = ApiException.class)
    public void addChannelOrderChannelSkuIdNotExistsErrorTest() throws ApiException {
        List<OrderItemFormChannel> orderItemFormChannelList = new ArrayList<>();

        Long clientId = testData.partyAdd(getRandomString(), PartyType.CLIENT).getId();
        Long customerId = testData.partyAdd(getRandomString(), PartyType.CUSTOMER).getId();
        Long channelId = testData.channelAdd().getId();

        for (int i = 0; i < 5; i++) {
            orderItemFormChannelList.add(testData.getOrderItemFormChannel(getRandomString(), getRandomNumberLong(),
                    getRandomNumberDouble()));
        }
        orderDto.addChannelOrder(testData.getChannelOrderForm(channelId, clientId, customerId, getRandomString(),
                orderItemFormChannelList));
    }

    @Test
    public void addDuplicateChannelOrderIdErrorTest() {
        List<OrderItemFormChannel> orderItemFormChannelList = new ArrayList<>();

        Long clientId = testData.partyAdd(getRandomString(), PartyType.CLIENT).getId();
        Long customerId = testData.partyAdd(getRandomString(), PartyType.CUSTOMER).getId();
        Long channelId = testData.channelAdd().getId();

        for (int i = 0; i < 5; i++) {
            String channelSkuId = testData.channelListAdd(channelId, clientId).getChannelSkuId();
            orderItemFormChannelList.add(testData.getOrderItemFormChannel(channelSkuId, getRandomNumberLong(),
                    getRandomNumberDouble()));
        }
        String channelOrderId = testData.orderAdd(clientId, channelId).getChannelOrderId();

        try {
            orderDto.addChannelOrder(testData.getChannelOrderForm(channelId, clientId, customerId, channelOrderId,
                    orderItemFormChannelList));
        } catch (ApiException e) {
            Assert.assertEquals("channel order id exists for the channel", e.getMessage());
        }
    }

    @Test
    public void addChannelOrderTest() throws ApiException {
        List<OrderItemFormChannel> orderItemFormChannelList = new ArrayList<>();

        Long clientId = testData.partyAdd(getRandomString(), PartyType.CLIENT).getId();
        Long customerId = testData.partyAdd(getRandomString(), PartyType.CUSTOMER).getId();
        Long channelId = testData.channelAdd().getId();

        for (int i = 0; i < 5; i++) {
            String channelSkuId = testData.channelListAdd(channelId, clientId).getChannelSkuId();
            orderItemFormChannelList.add(testData.getOrderItemFormChannel(channelSkuId, getRandomNumberLong(),
                    getRandomNumberDouble()));
        }

        orderDto.addChannelOrder(testData.getChannelOrderForm(channelId, clientId, customerId, getRandomString(),
                orderItemFormChannelList));
    }

//    @Test
//    public void fullAllocationOrderTest() throws ApiException {
//        OrderPojo orderPojo = testData.orderAdd();
//        orderPojo.setStatus(OrderStatus.CREATED);
//
//        OrderItemPojo orderItemPojo = testData.orderItemAdd(orderPojo.getId());
//        BinPojo binPojo = testData.binAdd();
//        testData.binSkuAdd(binPojo.getBinId(), orderItemPojo.getGlobalSkuId(), orderItemPojo.getOrderedQuantity());
//        testData.invAdd(orderItemPojo.getGlobalSkuId(), orderItemPojo.getOrderedQuantity(), 0L, 0L);
//
//        orderDto.allocateOrder(orderPojo.getId());
//        Assert.assertEquals(orderItemPojo.getOrderedQuantity(), orderItemPojo.getAllocatedQuantity());
//    }


}
