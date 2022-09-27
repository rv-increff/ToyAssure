package assure.dao;

import assure.pojo.BinPojo;
import assure.pojo.BinSkuPojo;
import assure.config.BaseTest;
import assure.util.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BinSkuDaoTest extends BaseTest {

    @Autowired
    private TestData testData;
    @Autowired
    private BinSkuDao binSkuDao;

    @Test
    public void addTest() {
        BinPojo binPojo = testData.binAdd();
        testData.binSkuAddByBinId(binPojo.getBinId());
    }

    @Test
    public void selectTest() {
        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) binSkuPojoList.add(testData.binSkuAdd());

        Assert.assertEquals(binSkuPojoList, testData.binSkuSelect());
    }

    @Test
    public void selectByGlobalSkuIdAndBinIdTest() {
        BinSkuPojo binSkuPojo = testData.binSkuAdd();
        Assert.assertEquals(binSkuPojo, binSkuDao.selectByGlobalSkuIdAndBinId(binSkuPojo.getGlobalSkuId(), binSkuPojo.getBinId()));
    }

    @Test
    public void selectByIdTest() {
        BinSkuPojo binSkuPojo = testData.binSkuAdd();
        Assert.assertEquals(binSkuPojo, binSkuDao.selectById(binSkuPojo.getId()));
    }

    @Test
    public void selectByGlobalSkuIdTest() {
        BinSkuPojo binSkuPojo = testData.binSkuAdd();
        Assert.assertEquals(binSkuPojo, binSkuDao.selectByGlobalSkuId(binSkuPojo.getGlobalSkuId()).get(0));
    }


}
