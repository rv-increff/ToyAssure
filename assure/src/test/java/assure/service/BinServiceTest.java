package assure.service;

import assure.pojo.BinPojo;
import assure.config.BaseTest;
import assure.util.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BinServiceTest extends BaseTest {
    @Autowired
    private BinService binService;
    @Autowired
    private TestData testData;

    @Test
    public void addTest() {
        Assert.assertEquals(binService.add(5), testData.binSelect());
    }

    @Test
    public void selectTest() {
        BinPojo binPojo = testData.binAdd();
        Assert.assertEquals(binPojo, binService.select(0, 1).get(0));
    }

    @Test
    public void selectByIdTest() {
        BinPojo binPojo = testData.binAdd();
        Assert.assertEquals(binPojo, binService.selectById(binPojo.getBinId()));
    }


}
