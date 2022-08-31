package assure.dao;

import assure.config.QaConfig;
import assure.pojo.BinSkuPojo;
import assure.pojo.ProductPojo;
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
public class BinSkuDaoTest extends AbstractTest{
    @Test
    public void addTest() {
        binAdd();
    }

    @Test
    public void selectTest(){
        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        for(int i=0;i<5;i++)binSkuPojoList.add(binSkuAdd());

        Assert.assertEquals(binSkuPojoList,binSkuSelect());
    }

    @Test
    public void selectByGlobalSkuIdAndBinIdTest(){
        BinSkuPojo binSkuPojo = binSkuAdd();
        Assert.assertEquals(binSkuPojo,binSkuDao.selectByGlobalSkuIdAndBinId(binSkuPojo.getGlobalSkuId(),binSkuPojo.getBinId()));
    }

     @Test
    public void selectByIdTest(){
        BinSkuPojo binSkuPojo = binSkuAdd();
        Assert.assertEquals(binSkuPojo,binSkuDao.selectById(binSkuPojo.getId()));
    }

     @Test
    public void selectByGlobalSkuIdTest(){
        BinSkuPojo binSkuPojo = binSkuAdd();
        Assert.assertEquals(binSkuPojo,binSkuDao.selectByGlobalSkuId(binSkuPojo.getGlobalSkuId()).get(0));
    }



}
