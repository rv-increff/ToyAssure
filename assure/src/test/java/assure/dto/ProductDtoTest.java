package assure.dto;

import assure.config.QaConfig;
import assure.util.AbstractTest;
import assure.model.ProductForm;
import assure.model.ProductUpdateForm;
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

import static assure.util.RandomUtil.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class ProductDtoTest extends AbstractTest {
    @Autowired
    private ProductDto productDto;

    @Test
    public void addNullListErrorTest() {
        List<ProductForm> productFormList = new ArrayList<>();
        try {
            productDto.add(productFormList, getRandomNumberLong());
        } catch (ApiException e) {
            Assert.assertEquals("Product Form List cannot be empty", e.getMessage());
        }
    }

    @Test
    public void addListSizeErrorTest() {
        List<ProductForm> productFormList = new ArrayList<>();
        int n = 10001;
        for (int i = 0; i < n; i++) {
            productFormList.add(new ProductForm());
        }
        try {
            productDto.add(productFormList, getRandomNumberLong());
        } catch (ApiException e) {
            Assert.assertEquals("Product Form List size more than limit, limit : 1000", e.getMessage());
        }
    }

    @Test(expected = ApiException.class)
    public void addNullInListTest() throws ApiException {
        List<ProductForm> productFormList = new ArrayList<>();
        int n = 10;
        for (int i = 0; i < n; i++) {
            productFormList.add(new ProductForm());
        }
        productDto.add(productFormList, getRandomNumberLong());
    }

    @Test(expected = ApiException.class)
    public void addDuplicateErrorTest() throws ApiException {
        List<ProductForm> productFormList = new ArrayList<>();
        int n = 10;
        String clientSkuId = getRandomString();
        for (int i = 0; i < n; i++) {
            ProductForm productForm = new ProductForm();
            productForm.setBrandId(getRandomString());
            productForm.setDescription(getRandomString());
            productForm.setName(getRandomString());
            productForm.setClientSkuId(clientSkuId);
            productFormList.add(productForm);
        }
        productDto.add(productFormList, getRandomNumberLong());
    }

    @Test
    public void addTest() throws ApiException {
        List<ProductForm> productFormList = new ArrayList<>();
        int n = 10;
        for (int i = 0; i < n; i++) {
            ProductForm productForm = new ProductForm();
            productForm.setBrandId(getRandomString());
            productForm.setDescription(getRandomString());
            productForm.setName(getRandomString());
            productForm.setClientSkuId(getRandomString());
            productFormList.add(productForm);
        }
        productDto.add(productFormList, partyAdd().getId());
    }

    @Test
    public void selectTest() {
        int n = 5;
        for (int i = 0; i < n; i++) {
            productAdd();
        }
        Assert.assertEquals(n, productDto.select(0).size());
    }

    @Test
    public void selectByIdTest() throws ApiException {
        ProductPojo productPojo = productAdd();
        Assert.assertEquals(productPojo.getGlobalSkuId(), productDto.selectById(productPojo.getGlobalSkuId()).getGlobalSkuId());
    }

    @Test(expected = ApiException.class)
    public void updateErrorTest() throws ApiException {
        ProductUpdateForm productUpdateForm = new ProductUpdateForm();
        productDto.update(productUpdateForm, getRandomNumberLong());
    }

    @Test
    public void updateTest() throws ApiException {
        ProductPojo productPojo = productAdd();
        ProductUpdateForm productUpdateForm = new ProductUpdateForm();
        productUpdateForm.setBrandId(getRandomString());
        productUpdateForm.setClientSkuId(getRandomString());
        productUpdateForm.setDescription(getRandomString());
        productUpdateForm.setName(getRandomString());


        Double newMrp = getRandomNumberDouble();
        productUpdateForm.setMrp(newMrp);
        productDto.update(productUpdateForm, productPojo.getGlobalSkuId());
        Assert.assertEquals(newMrp, productSelect().get(0).getMrp());
    }
}
