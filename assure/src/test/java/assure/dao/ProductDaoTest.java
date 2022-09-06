package assure.dao;

import assure.config.QaConfig;
import assure.pojo.ProductPojo;
import assure.util.AbstractTest;
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
public class ProductDaoTest extends AbstractTest {

    @Test
    public void addTest() {
        productAdd();
    }

    @Test
    public void selectTest(){
        List<ProductPojo> productPojoList = new ArrayList<>();
        for(int i=0;i<5;i++)productPojoList.add(productAdd());

        Assert.assertEquals(productPojoList,productSelect());
    }

    @Test
    public void selectByGlobalSkuIdTest(){
        ProductPojo productPojo = productAdd();
        Assert.assertEquals(productPojo,productDao.selectByGlobalSkuId(productPojo.getGlobalSkuId()));

    }

     @Test
    public void selectByClientIdTest(){
        ProductPojo productPojo = productAdd();
        Assert.assertEquals(productPojo,productDao.selectByClientId(productPojo.getClientId()).get(0));

    }
    @Test
    public void selectByClientIdAndClientSkuIdTest(){
        ProductPojo productPojo = productAdd();
        Assert.assertEquals(productPojo,productDao.selectByClientSkuIdAndClientId(productPojo.getClientSkuId(),
                productPojo.getClientId()));
    }

    @Test
    public void update(){
        productDao.update();
    }

}
