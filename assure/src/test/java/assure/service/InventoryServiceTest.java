package assure.service;

import assure.dao.InventoryDao;
import assure.pojo.InventoryPojo;
import assure.spring.ApiException;
import assure.util.BaseTest;
import assure.util.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static assure.util.RandomUtil.getRandomNumber;
import static assure.util.RandomUtil.getRandomNumberLong;
import static org.junit.Assert.fail;


public class InventoryServiceTest extends BaseTest {

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private TestData testData;
    @Autowired
    private InventoryDao inventoryDao;

    @Test
    public void addTest() {
        int n = 5;
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            inventoryPojoList.add(testData.getInv());
        }
        inventoryDao.add(inventoryPojoList.get(0));

        inventoryService.add(inventoryPojoList);

        Assert.assertEquals(inventoryPojoList.size(), testData.invSelect().size());
    }

    @Test
    public void selectByGlobalSkuIdTest() {
        InventoryPojo inventoryPojo = testData.invAdd();
        Assert.assertEquals(inventoryPojo, inventoryService.selectByGlobalSkuId(inventoryPojo.getGlobalSkuId()));
    }

    @Test
    public void allocateQtyTest() throws ApiException {
        InventoryPojo addedPojo = testData.invAdd();
        Long prevAllocatedQty = addedPojo.getAllocatedQuantity();
        Long allocateQty = addedPojo.getAvailableQuantity() - getRandomNumber() % addedPojo.getAvailableQuantity();
        inventoryService.allocateQty(allocateQty, addedPojo.getGlobalSkuId());
        InventoryPojo updatedPojo = inventoryDao.selectByGlobalSkuId(addedPojo.getGlobalSkuId());
        Assert.assertEquals((long) updatedPojo.getAllocatedQuantity(), allocateQty + prevAllocatedQty);

    }

    @Test
    public void fulfillQtyTest() throws ApiException {
        InventoryPojo addedPojo = testData.invAdd();
        Long previousFulfilledQty = addedPojo.getFulfilledQuantity();
        Long fulfilledQty = addedPojo.getAllocatedQuantity() - getRandomNumber() % addedPojo.getAllocatedQuantity();
        inventoryService.fulfillQty(fulfilledQty, addedPojo.getGlobalSkuId());
        InventoryPojo updatedPojo = inventoryDao.selectByGlobalSkuId(addedPojo.getGlobalSkuId());
        Assert.assertEquals(fulfilledQty + previousFulfilledQty, (long) updatedPojo.getFulfilledQuantity());
    }

    @Test
    public void getCheckByGlobalSkuIdTest() {
        try {
            inventoryService.getCheckByGlobalSkuId(getRandomNumberLong());
            fail("error should be thrown");
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Inventory with global sku Id doesn't exists ");
        }
    }
}
