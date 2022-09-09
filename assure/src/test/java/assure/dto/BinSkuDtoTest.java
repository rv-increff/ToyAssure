package assure.dto;

import assure.config.QaConfig;
import assure.util.AbstractTest;
import assure.model.BinSkuForm;
import assure.model.BinSkuItemForm;
import assure.model.BinSkuUpdateForm;
import assure.pojo.BinSkuPojo;
import assure.pojo.PartyPojo;
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

import static assure.util.RandomUtil.getRandomNumberLong;
import static assure.util.RandomUtil.getRandomString;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class BinSkuDtoTest extends AbstractTest {

    @Autowired
    private BinSkuDto binSkuDto;

    @Test
    public void addValidationEmptyListErrorTest() {
        BinSkuForm binSkuForm = new BinSkuForm();
        try {
            binSkuDto.add(binSkuForm);
            fail("error should be thrown");
        } catch (ApiException e) {
            Assert.assertEquals("BinSku List cannot be empty", e.getMessage());
        }
    }

    @Test
    public void addValidationMaxListSizeErrorTest() {
        BinSkuForm binSkuForm = new BinSkuForm();
        try {
            List<BinSkuItemForm> binSkuItemFormList = new ArrayList<>();
            for (int i = 0; i < 101; i++) {
                binSkuItemFormList.add(new BinSkuItemForm());
            }
            binSkuForm.setBinSkuItemFormList(binSkuItemFormList);
            binSkuDto.add(binSkuForm);
            fail("error should be thrown");
        } catch (ApiException e) {
            Assert.assertEquals("BinSku List size more than limit, limit : 100", e.getMessage());
        }

    }

    @Test(expected = ApiException.class)
    public void addValidationNullValueInListErrorTest() throws ApiException {
        BinSkuForm binSkuForm = new BinSkuForm();

        List<BinSkuItemForm> binSkuItemFormList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            binSkuItemFormList.add(new BinSkuItemForm());
        }
        binSkuForm.setBinSkuItemFormList(binSkuItemFormList);
        binSkuDto.add(binSkuForm);
    }

    @Test(expected = ApiException.class)
    public void addBinIdExistsError() throws ApiException {
        BinSkuForm binSkuForm = new BinSkuForm();

        List<BinSkuItemForm> binSkuItemFormList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BinSkuItemForm binSkuItemForm = new BinSkuItemForm();
            binSkuItemForm.setBinId(getRandomNumberLong());
            binSkuItemForm.setClientSkuId(getRandomString());
            binSkuItemForm.setQuantity(getRandomNumberLong());
            binSkuItemFormList.add(binSkuItemForm);
        }
        binSkuForm.setBinSkuItemFormList(binSkuItemFormList);
        binSkuDto.add(binSkuForm);
    }

    @Test
    public void addClientErrorTest() {
        BinSkuForm binSkuForm = new BinSkuForm();

        List<BinSkuItemForm> binSkuItemFormList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BinSkuItemForm binSkuItemForm = new BinSkuItemForm();
            binSkuItemForm.setBinId(binAdd().getBinId());
            binSkuItemForm.setClientSkuId(getRandomString());
            binSkuItemForm.setQuantity(getRandomNumberLong());
            binSkuItemFormList.add(binSkuItemForm);
        }
        binSkuForm.setBinSkuItemFormList(binSkuItemFormList);
        binSkuForm.setClientId(getRandomNumberLong());
        try {
            binSkuDto.add(binSkuForm);
            fail("error should be thrown");
        } catch (ApiException e) {
            Assert.assertEquals("Party does not exist", e.getMessage());
        }
    }

    @Test(expected = ApiException.class)
    public void addClientSkuIdErrorTest() throws ApiException {
        BinSkuForm binSkuForm = new BinSkuForm();

        List<BinSkuItemForm> binSkuItemFormList = new ArrayList<>();
        PartyPojo partyPojo = partyAdd();

        for (int i = 0; i < 5; i++) {
            BinSkuItemForm binSkuItemForm = new BinSkuItemForm();
            binSkuItemForm.setBinId(binAdd().getBinId());
            binSkuItemForm.setClientSkuId(getRandomString());
            binSkuItemForm.setQuantity(getRandomNumberLong());
            binSkuItemFormList.add(binSkuItemForm);
        }
        binSkuForm.setBinSkuItemFormList(binSkuItemFormList);
        binSkuForm.setClientId(partyPojo.getId());

        binSkuDto.add(binSkuForm);

    }

    @Test
    public void addTest() throws ApiException {
        BinSkuForm binSkuForm = new BinSkuForm();
        PartyPojo partyPojo = partyAdd();
        List<BinSkuItemForm> binSkuItemFormList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BinSkuItemForm binSkuItemForm = new BinSkuItemForm();
            binSkuItemForm.setBinId(binAdd().getBinId());
            binSkuItemForm.setClientSkuId(productAdd(partyPojo.getId()).getClientSkuId());
            binSkuItemForm.setQuantity(getRandomNumberLong());
            binSkuItemFormList.add(binSkuItemForm);
        }

        binSkuForm.setBinSkuItemFormList(binSkuItemFormList);
        binSkuForm.setClientId(partyPojo.getId());
        binSkuDto.add(binSkuForm);
    }

    @Test
    public void selectTest() {
        int n = 5;
        for (int i = 0; i < 5; i++) {
            binSkuAdd(productAdd().getGlobalSkuId());
        }
        Assert.assertEquals(n, binSkuDto.select(0).size());
    }

    @Test
    public void updateTest() throws ApiException {
        BinSkuPojo binSkuPojo = binSkuAdd();
        BinSkuUpdateForm binSkuUpdateForm = new BinSkuUpdateForm();
        Long newQty = getRandomNumberLong();
        binSkuUpdateForm.setQuantity(newQty);
        binSkuDto.update(binSkuUpdateForm, binSkuPojo.getId());
        Assert.assertEquals(newQty, binSkuSelect().get(0).getQuantity());

    }

}
