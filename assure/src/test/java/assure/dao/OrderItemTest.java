package assure.dao;

import assure.config.QaConfig;
import assure.pojo.OrderItemPojo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static assure.util.RandomUtil.getRandomNumberLong;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class OrderItemTest extends AbstractTest {
    @Test
    public void addTest() {
        orderItemAdd(getRandomNumberLong());
    }

    @Test
    public void selectByIdTest() {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        Long orderId = getRandomNumberLong();
        for (int i = 0; i < 5; i++) orderItemPojoList.add(orderItemAdd(orderId));
        List<OrderItemPojo> orderItemPojoById = orderItemDao.selectByOrderId(orderId);
        Assert.assertEquals(orderItemPojoList.stream().collect(Collectors.toSet()), orderItemPojoById.stream().collect(Collectors.toSet()));
    }
}
