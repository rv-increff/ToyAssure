package assure.service;

import assure.config.QaConfig;
import assure.dao.AbstractTest;
import assure.pojo.ProductPojo;
import assure.spring.ApiException;
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

import static assure.util.RandomUtil.getRandomNumberDouble;
import static assure.util.RandomUtil.getRandomNumberLong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class ProductServiceTest extends AbstractTest {
    @Autowired
    private ProductService productService;

    @Test
    public void addTest() throws ApiException {
        List<ProductPojo> productPojoList = new ArrayList<>();
        Long clientId = getRandomNumberLong();
        for (int i = 0; i < 5; i++) {
            productPojoList.add(getProduct(clientId, i + ""));
        }
        productService.add(productPojoList);
    }

    @Test
    public void addClientSkuIdDuplicateErrorTest() {
        List<ProductPojo> productPojoList = new ArrayList<>();
        ProductPojo productPojo = productAdd();
        for (int i = 0; i < 5; i++) {
            productPojoList.add(getProduct(productPojo.getClientId(), productPojo.getClientSkuId()));
        }
        try {
            productService.add(productPojoList);
            fail("error not thrown");
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "clientSkuId - clientId pair exists");
        }
    }

    @Test
    public void selectTest() {
        ProductPojo productPojo = productAdd();
        Assert.assertEquals(productPojo, productService.select(0, 1).get(0));
    }

    @Test
    public void selectByIdTest() throws ApiException {
        ProductPojo productPojo = productAdd();
        Assert.assertEquals(productPojo, productService.selectById(productPojo.getGlobalSkuId()));
    }

    @Test
    public void selectByIdErrorTest() {
        ProductPojo productPojo = productAdd();
        try {
            Assert.assertEquals(productPojo, productService.selectById(productPojo.getGlobalSkuId() + 1));
            fail("error not thrown");
        } catch (ApiException e) {
            Assert.assertEquals("product with id does not exist", e.getMessage());
        }
    }

    @Test
    public void selectByClientIdTest() {
        List<ProductPojo> productPojoList = new ArrayList<>();
        ProductPojo productPojo = productAdd();
        productPojoList.add(productPojo);
        for (int i = 0; i < 5; i++) {
            ProductPojo productPojo1 = getProduct(productPojo.getClientId(), i + "");
            productPojoList.add(productDao.add(productPojo1));
        }
        Assert.assertEquals(productPojoList, productService.selectByClientId(productPojo.getClientId()));
    }

    @Test
    public void updateTest() throws ApiException {
        ProductPojo productPojo = productAdd();
        Double mrp = getRandomNumberDouble();
        productPojo.setMrp(mrp);
        productService.update(productPojo);
    }

    @Test
    public void updateErrorTest() {
        ProductPojo productPojo1 = productAdd();
        ProductPojo productPojo = productAdd();
        Double mrp = getRandomNumberDouble();
        ProductPojo newProductPojo = getProduct(productPojo1.getClientId(),productPojo1.getClientSkuId());
        newProductPojo.setGlobalSkuId(productPojo.getGlobalSkuId());
        try {
            productService.update(newProductPojo);
            fail("error not thrown");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "clientSkuId - clientId pair exists");
        }
    }

    @Test
    public void selectByClientSkuIdAndClientIdTest() {
        ProductPojo productPojo = productAdd();
        Assert.assertEquals(productPojo, productService.selectByClientSkuIdAndClientId
                (productPojo.getClientSkuId(), productPojo.getClientId()));
    }

    @Test
    public void selectByGlobalSkuId(){
        ProductPojo productPojo = productAdd();
        Assert.assertEquals(productPojo, productService.selectByGlobalSkuId(productPojo.getGlobalSkuId()));
    }
}
