package assure.dao;

import assure.pojo.InventoryPojo;
import assure.config.BaseTest;
import assure.util.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
