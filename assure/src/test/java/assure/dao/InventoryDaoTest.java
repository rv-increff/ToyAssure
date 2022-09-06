package assure.dao;

import assure.config.QaConfig;
import assure.pojo.InventoryPojo;
import assure.util.AbstractTest;
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
public class InventoryDaoTest extends AbstractTest {
    @Test
    public void addTest() {
        invAdd();
    }

    @Test
    public void selectTest() {
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) inventoryPojoList.add(invAdd());

        Assert.assertEquals(inventoryPojoList, invSelect());
    }

    @Test
    public void selectByGlobalSkuIdTest() {
        InventoryPojo inventoryPojo = invAdd();
        Assert.assertEquals(inventoryPojo, inventoryDao.selectByGlobalSkuId(inventoryPojo.getGlobalSkuId()));
    }
}
