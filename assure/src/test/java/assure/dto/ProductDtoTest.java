package assure.dto;

import assure.config.BaseTest;
import assure.pojo.PartyPojo;
import assure.util.TestData;
import assure.model.ProductForm;
import assure.model.ProductUpdateForm;
import assure.pojo.ProductPojo;
import assure.spring.ApiException;
import assure.util.RandomUtil;
import commons.util.PartyType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ProductDtoTest extends BaseTest {
    @Autowired
    private ProductDto productDto;
    @Autowired
    private TestData testData;

    @Test
    public void addNullListErrorTest() {
        List<ProductForm> productFormList = new ArrayList<>();
        try {
            productDto.add(productFormList, RandomUtil.getRandomNumberLong());
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
            productDto.add(productFormList, RandomUtil.getRandomNumberLong());
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
        productDto.add(productFormList, RandomUtil.getRandomNumberLong());
    }

    @Test(expected = ApiException.class)
    public void addDuplicateErrorTest() throws ApiException {
        List<ProductForm> productFormList = new ArrayList<>();
        int n = 10;
        String clientSkuId = RandomUtil.getRandomString();
        for (int i = 0; i < n; i++) {
            ProductForm productForm = new ProductForm();
            productForm.setBrandId(RandomUtil.getRandomString());
            productForm.setDescription(RandomUtil.getRandomString());
            productForm.setName(RandomUtil.getRandomString());
            productForm.setClientSkuId(clientSkuId);
            productFormList.add(productForm);
        }
        productDto.add(productFormList, RandomUtil.getRandomNumberLong());
    }

    @Test
    public void addTest() throws ApiException {
        List<ProductForm> productFormList = new ArrayList<>();
        int n = 10;
        for (int i = 0; i < n; i++) {
            ProductForm productForm = new ProductForm();
            productForm.setBrandId(RandomUtil.getRandomString());
            productForm.setDescription(RandomUtil.getRandomString());
            productForm.setName(RandomUtil.getRandomString());
            productForm.setClientSkuId(RandomUtil.getRandomString());
            productForm.setMrp(RandomUtil.getRandomNumberDouble());
            productFormList.add(productForm);
        }
        productDto.add(productFormList, testData.partyAdd().getId());
    }

    @Test
    public void selectTest() throws ApiException {
        int n = 5;
        for (int i = 0; i < n; i++) {
            testData.productAdd(testData.partyAdd(RandomUtil.getRandomString(), PartyType.CLIENT).getId());
        }
        Assert.assertEquals(n, productDto.select(0).size());
    }

    @Test
    public void selectByIdTest() throws ApiException {
        ProductPojo productPojo = testData.productAdd(testData.partyAdd(RandomUtil.getRandomString(), PartyType.CLIENT).getId());
        Assert.assertEquals(productPojo.getGlobalSkuId(), productDto.selectById(productPojo.getGlobalSkuId()).getGlobalSkuId());
    }

    @Test(expected = ApiException.class)
    public void updateErrorTest() throws ApiException {
        ProductUpdateForm productUpdateForm = new ProductUpdateForm();
        productDto.update(productUpdateForm, RandomUtil.getRandomNumberLong());
    }

    @Test
    public void updateTest() throws ApiException {
        ProductPojo productPojo = testData.productAdd();
        ProductUpdateForm productUpdateForm = new ProductUpdateForm();
        productUpdateForm.setBrandId(RandomUtil.getRandomString());
        productUpdateForm.setDescription(RandomUtil.getRandomString());
        productUpdateForm.setName(RandomUtil.getRandomString());


        Double newMrp = RandomUtil.getRandomNumberDouble();
        productUpdateForm.setMrp(newMrp);
        productDto.update(productUpdateForm, productPojo.getGlobalSkuId());
        Assert.assertEquals(newMrp, testData.productSelect().get(0).getMrp());
    }
}
