package assure.dao;

import assure.pojo.BinPojo;
import assure.config.BaseTest;
import assure.util.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BinDaoTest extends BaseTest {

    @Autowired
    private BinDao binDao;
    @Autowired
    private TestData testData;

    @Test
    public void addTest(){
        testData.binAdd();
    }

    @Test
    public void selectTest(){
        List<BinPojo> binPojoList = new ArrayList<>();
        for(int i=0;i<5;i++)
            binPojoList.add(testData.binAdd());

        Assert.assertEquals(binPojoList, testData.binSelect());
    }

    @Test
    public void selectByIdTest(){
        BinPojo binPojo = testData.binAdd();
        Assert.assertEquals(binPojo, binDao.selectById(binPojo.getBinId()));
    }


}
