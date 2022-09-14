package assure.dao;

import assure.pojo.OrderPojo;
import assure.config.BaseTest;
import assure.util.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class OrderDaoTest extends BaseTest {

    @Autowired
    private TestData testData;
    @Autowired
    private OrderDao orderDao;

    @Test
    public void addTest() {
        testData.orderAdd();
    }

    @Test
    public void selectTest() {
        List<OrderPojo> orderPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) orderPojoList.add(testData.orderAdd());

        Assert.assertEquals(orderPojoList, testData.orderSelect());
    }

    @Test
    public void selectByChannelIdAndChannelOrderIdTest() {
        OrderPojo orderPojo = testData.orderAdd();
        Assert.assertEquals(orderPojo, orderDao.selectByChannelIdAndChannelOrderId(orderPojo.getChannelId(), orderPojo.getChannelOrderId()));

    }

    @Test
    public void selectByIdTest() {
        OrderPojo orderPojo = testData.orderAdd();
        Assert.assertEquals(orderPojo, orderDao.selectById(orderPojo.getId()));

    }

}
