package assure.service;

import assure.config.QaConfig;
import assure.util.AbstractTest;
import assure.pojo.InventoryPojo;
import assure.spring.ApiException;
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

import static assure.util.RandomUtil.getRandomNumber;
import static assure.util.RandomUtil.getRandomNumberLong;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class InventoryServiceTest extends AbstractTest {

    @Autowired
    private InventoryService inventoryService;

    @Test
    public void addTest(){
        int n=5;
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            inventoryPojoList.add(getInv());
        }
        inventoryDao.add(inventoryPojoList.get(0));

        inventoryService.add(inventoryPojoList);

        Assert.assertEquals(inventoryPojoList.size(), invSelect().size());
    }

    @Test
    public void selectByGlobalSkuIdTest(){
        InventoryPojo inventoryPojo = invAdd();
        Assert.assertEquals(inventoryPojo, inventoryService.selectByGlobalSkuId(inventoryPojo.getGlobalSkuId()));
    }

    @Test
    public void allocateQtyTest() throws ApiException {
        InventoryPojo addedPojo = invAdd();
        Long prevAllocatedQty = addedPojo.getAllocatedQuantity();
        Long allocateQty = addedPojo.getAvailableQuantity() - getRandomNumber() % addedPojo.getAvailableQuantity();
        inventoryService.allocateQty(allocateQty, addedPojo.getGlobalSkuId());
        InventoryPojo updatedPojo = inventoryDao.selectByGlobalSkuId(addedPojo.getGlobalSkuId());
        Assert.assertEquals((long)updatedPojo.getAllocatedQuantity(),allocateQty + prevAllocatedQty);

    }

    @Test
    public void fulfillQtyTest() throws ApiException {
        InventoryPojo addedPojo = invAdd();
        Long previousFulfilledQty = addedPojo.getFulfilledQuantity();
        Long fulfilledQty = addedPojo.getAllocatedQuantity() - getRandomNumber() % addedPojo.getAllocatedQuantity();
        inventoryService.fulfillQty(fulfilledQty, addedPojo.getGlobalSkuId());
        InventoryPojo updatedPojo = inventoryDao.selectByGlobalSkuId(addedPojo.getGlobalSkuId());
        Assert.assertEquals(fulfilledQty + previousFulfilledQty,(long)updatedPojo.getFulfilledQuantity());
    }

    @Test
    public void getCheckByGlobalSkuIdTest(){
        try{
            inventoryService.getCheckByGlobalSkuId(getRandomNumberLong());
            fail("error should be thrown");
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Inventory with global sku Id doesn't exists ");
        }
    }
}
