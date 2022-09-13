package assure.dao;

import assure.config.QaConfig;
import assure.pojo.ProductPojo;
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

public class ProductDaoTest extends BaseTest {

    @Autowired
    private TestData testData;
    @Autowired
    private ProductDao productDao;

    @Test
    public void addTest() {
        testData.productAdd();
    }

    @Test
    public void selectTest(){
        List<ProductPojo> productPojoList = new ArrayList<>();
        for(int i=0;i<5;i++)productPojoList.add(testData.productAdd());

        Assert.assertEquals(productPojoList,testData.productSelect());
    }

    @Test
    public void selectByGlobalSkuIdTest(){
        ProductPojo productPojo = testData.productAdd();
        Assert.assertEquals(productPojo,productDao.selectByGlobalSkuId(productPojo.getGlobalSkuId()));

    }

     @Test
    public void selectByClientIdTest(){
        ProductPojo productPojo = testData.productAdd();
        Assert.assertEquals(productPojo,productDao.selectByClientId(productPojo.getClientId()).get(0));

    }
    @Test
    public void selectByClientIdAndClientSkuIdTest(){
        ProductPojo productPojo = testData.productAdd();
        Assert.assertEquals(productPojo,productDao.selectByClientSkuIdAndClientId(productPojo.getClientSkuId(),
                productPojo.getClientId()));
    }

    @Test
    public void update(){
        productDao.update();
    }

}
