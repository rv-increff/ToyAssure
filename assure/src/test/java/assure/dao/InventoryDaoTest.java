package assure.dao;

import assure.config.QaConfig;
import assure.pojo.InventoryPojo;
import assure.util.BaseTest;
import assure.util.TestData;
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
import java.util.List;

public class InventoryDaoTest extends BaseTest {

    @Autowired
    private TestData testData;
    @Autowired
    private InventoryDao inventoryDao;

    @Test
    public void addTest() {
        testData.invAdd();
    }

    @Test
    public void selectTest() {
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) inventoryPojoList.add(testData.invAdd());

        Assert.assertEquals(inventoryPojoList, testData.invSelect());
    }

    @Test
    public void selectByGlobalSkuIdTest() {
        InventoryPojo inventoryPojo = testData.invAdd();
        Assert.assertEquals(inventoryPojo, inventoryDao.selectByGlobalSkuId(inventoryPojo.getGlobalSkuId()));
    }
}
