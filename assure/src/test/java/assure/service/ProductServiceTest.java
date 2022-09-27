package assure.service;

import assure.dao.ProductDao;
import assure.config.BaseTest;
import assure.util.TestData;
import assure.pojo.ProductPojo;
import assure.spring.ApiException;
import assure.util.RandomUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ProductServiceTest extends BaseTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private TestData testData;
    @Autowired
    private ProductDao productDao;

    @Test
    public void addTest() throws ApiException {
        List<ProductPojo> productPojoList = new ArrayList<>();
        Long clientId = RandomUtil.getRandomNumberLong();
        for (int i = 0; i < 5; i++) {
            productPojoList.add(testData.getProduct(clientId, i + ""));
        }
        productService.add(productPojoList);
    }

    @Test
    public void addClientSkuIdDuplicateErrorTest() {
        List<ProductPojo> productPojoList = new ArrayList<>();
        ProductPojo productPojo = testData.productAdd();
        for (int i = 0; i < 5; i++) {
            productPojoList.add(testData.getProduct(productPojo.getClientId(), productPojo.getClientSkuId()));
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
        ProductPojo productPojo = testData.productAdd();
        Assert.assertEquals(productPojo, productService.select(0, 1).get(0));
    }

    @Test
    public void selectByIdTest() throws ApiException {
        ProductPojo productPojo = testData.productAdd();
        Assert.assertEquals(productPojo, productService.selectById(productPojo.getGlobalSkuId()));
    }

    @Test
    public void selectByIdErrorTest() {
        ProductPojo productPojo = testData.productAdd();
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
        ProductPojo productPojo = testData.productAdd();
        productPojoList.add(productPojo);
        for (int i = 0; i < 5; i++) {
            ProductPojo productPojo1 = testData.getProduct(productPojo.getClientId(), i + "");
            productPojoList.add(productDao.add(productPojo1));
        }
        Assert.assertEquals(productPojoList, productService.selectByClientId(productPojo.getClientId()));
    }

    @Test
    public void updateTest() throws ApiException {
        ProductPojo productPojo = testData.productAdd();
        Double mrp = RandomUtil.getRandomNumberDouble();
        productPojo.setMrp(mrp);
        productService.update(productPojo);
    }

    @Test
    public void updateErrorTest() {
        ProductPojo productPojo1 = testData.productAdd();
        ProductPojo productPojo = testData.productAdd();
        Double mrp = RandomUtil.getRandomNumberDouble();
        ProductPojo newProductPojo = testData.getProduct(productPojo1.getClientId(),productPojo1.getClientSkuId());
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
        ProductPojo productPojo = testData.productAdd();
        Assert.assertEquals(productPojo, productService.selectByClientSkuIdAndClientId
                (productPojo.getClientSkuId(), productPojo.getClientId()));
    }

    @Test
    public void selectByGlobalSkuId(){
        ProductPojo productPojo = testData.productAdd();
        Assert.assertEquals(productPojo, productService.selectByGlobalSkuId(productPojo.getGlobalSkuId()));
    }
}
