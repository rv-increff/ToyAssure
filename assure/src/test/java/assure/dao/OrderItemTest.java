package assure.dao;

import assure.pojo.OrderItemPojo;
import assure.config.BaseTest;
import assure.util.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static assure.util.RandomUtil.getRandomNumberLong;

public class OrderItemTest extends BaseTest {

    @Autowired
    private TestData testData;
    @Autowired
    private OrderItemDao orderItemDao;

    @Test
    public void addTest() {
        testData.orderItemAdd(getRandomNumberLong());
    }

    @Test
    public void selectByIdTest() {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        Long orderId = getRandomNumberLong();
        for (int i = 0; i < 5; i++) orderItemPojoList.add(testData.orderItemAdd(orderId));
        List<OrderItemPojo> orderItemPojoById = orderItemDao.selectByOrderId(orderId);
        Assert.assertEquals(new HashSet<>(orderItemPojoList), new HashSet<>(orderItemPojoById));
    }
}
