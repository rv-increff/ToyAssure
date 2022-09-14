package assure.dao;

import assure.pojo.ProductPojo;
import assure.config.BaseTest;
import assure.util.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
