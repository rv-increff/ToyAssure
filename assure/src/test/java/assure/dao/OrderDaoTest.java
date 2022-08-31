package assure.dao;

import assure.config.QaConfig;
import assure.pojo.BinSkuPojo;
import assure.pojo.OrderPojo;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class OrderDaoTest extends AbstractTest{
    @Test
    public void addTest() {
        orderAdd();
    }

    @Test
    public void selectTest(){
        List<OrderPojo> orderPojoList = new ArrayList<>();
        for(int i=0;i<5;i++)orderPojoList.add(orderAdd());

        Assert.assertEquals(orderPojoList,orderSelect());
    }

    @Test
    public void selectByChannelIdAndChannelOrderIdTest(){
        OrderPojo orderPojo = orderAdd();
        Assert.assertEquals(orderPojo,orderDao.selectByChannelIdAndChannelOrderId(orderPojo.getChannelId(), orderPojo.getChannelOrderId()));

    }
 @Test
    public void selectByIdTest(){
        OrderPojo orderPojo = orderAdd();
        Assert.assertEquals(orderPojo,orderDao.selectById(orderPojo.getId()));

    }

}
