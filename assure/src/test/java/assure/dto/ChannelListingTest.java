package assure.dto;

import assure.config.QaConfig;
import assure.util.BaseTest;
import assure.util.TestData;
import assure.model.ChannelListingForm;
import assure.model.ChannelListingUploadForm;
import assure.pojo.ChannelPojo;
import assure.pojo.PartyPojo;
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

import static assure.util.RandomUtil.getRandomNumberLong;
import static assure.util.RandomUtil.getRandomString;
import static org.junit.Assert.fail;


public class ChannelListingTest extends BaseTest {

    @Autowired
    private ChannelListingDto channelListingDto;
    @Autowired
    private TestData testData;

    @Test(expected = ApiException.class)
    public void addEmptyErrorTest() throws ApiException {

        ChannelListingUploadForm channelListingUploadForm = new ChannelListingUploadForm();
        channelListingDto.add(channelListingUploadForm);
    }

    @Test
    public void addListSizeTest() {

        ChannelListingUploadForm channelListingUploadForm = new ChannelListingUploadForm();
        List<ChannelListingForm> channelListingFormList = new ArrayList<>();
        for (int i = 0; i < 1001; i++) {
            ChannelListingForm channelListingForm = new ChannelListingForm();
            channelListingForm.setChannelSkuId(getRandomString());
            channelListingForm.setClientSkuId(getRandomString());
            channelListingFormList.add(channelListingForm);
        }

        channelListingUploadForm.setChannelListingFormList(channelListingFormList);
        channelListingUploadForm.setChannelId(getRandomNumberLong());
        channelListingUploadForm.setClientId(getRandomNumberLong());

        try{
            channelListingDto.add(channelListingUploadForm);
            fail("error should be thrown");
        }catch (ApiException e){
            Assert.assertEquals("Channel Listing Upload List size more than limit, limit : 1000",e.getLocalizedMessage());
        }
    }
    @Test(expected = ApiException.class)
    public void addDuplicateTest() throws ApiException {

        ChannelListingUploadForm channelListingUploadForm = new ChannelListingUploadForm();
        List<ChannelListingForm> channelListingFormList = new ArrayList<>();
        String channelSkuId = getRandomString();
        String clientSkuId = getRandomString();
        for (int i = 0; i < 10; i++) {
            ChannelListingForm channelListingForm = new ChannelListingForm();
            channelListingForm.setChannelSkuId(channelSkuId);
            channelListingForm.setClientSkuId(clientSkuId);
            channelListingFormList.add(channelListingForm);
        }
        channelListingUploadForm.setChannelListingFormList(channelListingFormList);
        channelListingDto.add(channelListingUploadForm);

    }

    @Test(expected = ApiException.class)
    public void addClientSkuIdNotExistsErrorTest() throws ApiException {
        PartyPojo partyPojo = testData.partyAdd();
        ChannelPojo channelPojo = testData.channelAdd();
        ChannelListingUploadForm channelListingUploadForm = new ChannelListingUploadForm();
        List<ChannelListingForm> channelListingFormList = new ArrayList<>();

        channelListingUploadForm.setChannelId(channelPojo.getId());
        channelListingUploadForm.setClientId(partyPojo.getId());

        for (int i = 0; i < 10; i++) {
            ChannelListingForm channelListingForm = new ChannelListingForm();
            ProductPojo productPojo = testData.productAdd(partyPojo.getId() + 1);
            //add products
            channelListingForm.setChannelSkuId(getRandomString());
            channelListingForm.setClientSkuId(productPojo.getClientSkuId());
            channelListingFormList.add(channelListingForm);
        }
        channelListingUploadForm.setChannelListingFormList(channelListingFormList);
        channelListingDto.add(channelListingUploadForm);
    }
    @Test
    public void addTest() throws ApiException {
        PartyPojo partyPojo = testData.partyAdd();
        ChannelPojo channelPojo = testData.channelAdd();
        ChannelListingUploadForm channelListingUploadForm = new ChannelListingUploadForm();
        List<ChannelListingForm> channelListingFormList = new ArrayList<>();

        channelListingUploadForm.setChannelId(channelPojo.getId());
        channelListingUploadForm.setClientId(partyPojo.getId());

        for (int i = 0; i < 10; i++) {
            ChannelListingForm channelListingForm = new ChannelListingForm();
            ProductPojo productPojo = testData.productAdd(partyPojo.getId());
            //add products
            channelListingForm.setChannelSkuId(getRandomString());
            channelListingForm.setClientSkuId(productPojo.getClientSkuId());
            channelListingFormList.add(channelListingForm);
        }
        channelListingUploadForm.setChannelListingFormList(channelListingFormList);
        channelListingDto.add(channelListingUploadForm);
    }

    @Test
    public void selectTest(){
        int n = 5;
        for (int i = 0; i < n; i++) {
            testData.channelListAdd();
        }
        Assert.assertEquals(channelListingDto.select(0).size(), n);
    }
}
