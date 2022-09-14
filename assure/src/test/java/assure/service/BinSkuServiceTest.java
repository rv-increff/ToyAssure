package assure.service;

import assure.pojo.BinPojo;
import assure.pojo.BinSkuPojo;
import assure.spring.ApiException;
import assure.config.BaseTest;
import assure.util.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;


public class BinSkuServiceTest extends BaseTest {

    @Autowired
    private BinSkuService binSkuService;
    @Autowired
    private TestData testData;

//    @Test
//    public void add() throws ApiException {
//        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
//        Long binId = testData.binAdd().getBinId();
//        for (int i = 0; i < 5; i++) {
//            BinSkuPojo binSkuPojo = testData.getBinSku();
//            binSkuPojo.setId(binId);
//            binSkuPojoList.add(testData.getBinSku());
//        }
//        binSkuService.add(binSkuPojoList);
//    }

    @Test
    public void addDuplicateError() {
        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BinSkuPojo binSkuPojo = testData.getBinSku();
            binSkuPojo.setBinId(testData.binSkuAdd().getBinId());
            binSkuPojoList.add(binSkuPojo);
        }
        binSkuPojoList.add(binSkuPojoList.get(0));
        try {
            binSkuService.add(binSkuPojoList);
            fail("error not thrown");
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "duplicate values of globalSkuId-binId pair");
        }
    }

    @Test
    public void updateTest() throws ApiException {
        BinSkuPojo binSkuPojo = testData.binSkuAdd();
        BinSkuPojo updateBinSkuPojo = testData.getBinSku();
        updateBinSkuPojo.setId(binSkuPojo.getId());
        binSkuService.update(updateBinSkuPojo);
    }

    @Test
    public void updateError() {
        BinSkuPojo binSkuPojo = testData.getBinSku();
        binSkuPojo.setId(1L);
        try {
            binSkuService.update(binSkuPojo);
            fail("error expected");
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "id doesn't exist, id : " + binSkuPojo.getId());
        }
    }

    @Test
    public void select() {
        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            binSkuPojoList.add(testData.binSkuAdd());
        }
        Assert.assertEquals(binSkuPojoList, binSkuService.select(0, 5));
    }

    @Test
    public void allocateQtyTest() {
        BinSkuPojo binSkuPojo = testData.binSkuAdd();

        binSkuService.allocateQty(binSkuPojo.getQuantity(), binSkuPojo.getGlobalSkuId());
        Assert.assertEquals((long) binSkuPojo.getQuantity(), 0L);
    }


}
