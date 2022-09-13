package assure.dao;

import assure.config.QaConfig;
import assure.pojo.BinSkuPojo;
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

public class BinSkuDaoTest extends BaseTest {

    @Autowired
    private TestData testData;
    @Autowired
    private BinSkuDao binSkuDao;

    @Test
    public void addTest() {
        testData.binAdd();
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
