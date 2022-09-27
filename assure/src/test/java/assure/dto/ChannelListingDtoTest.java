package assure.dto;

import assure.config.BaseTest;
import assure.util.TestData;
import assure.model.ChannelListingForm;
import assure.model.ChannelListingUploadForm;
import assure.pojo.ChannelPojo;
import assure.pojo.PartyPojo;
import assure.pojo.ProductPojo;
import assure.spring.ApiException;
import assure.util.RandomUtil;
import commons.util.PartyType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static assure.util.RandomUtil.getRandomString;
import static org.junit.Assert.fail;


public class ChannelListingDtoTest extends BaseTest {

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
            channelListingForm.setChannelSkuId(RandomUtil.getRandomString());
            channelListingForm.setClientSkuId(RandomUtil.getRandomString());
            channelListingFormList.add(channelListingForm);
        }

        channelListingUploadForm.setChannelListingFormList(channelListingFormList);
        channelListingUploadForm.setChannelId(RandomUtil.getRandomNumberLong());
        channelListingUploadForm.setClientId(RandomUtil.getRandomNumberLong());

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
        String channelSkuId = RandomUtil.getRandomString();
        String clientSkuId = RandomUtil.getRandomString();
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
            channelListingForm.setChannelSkuId(RandomUtil.getRandomString());
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
            channelListingForm.setChannelSkuId(RandomUtil.getRandomString());
            channelListingForm.setClientSkuId(productPojo.getClientSkuId());
            channelListingFormList.add(channelListingForm);
        }
        channelListingUploadForm.setChannelListingFormList(channelListingFormList);
        channelListingDto.add(channelListingUploadForm);
    }

    @Test
    public void selectTest() throws ApiException {
        int n = 5;
        ChannelPojo channelPojo = testData.channelAdd();
        PartyPojo partyPojo = testData.partyAdd(RandomUtil.getRandomString(), PartyType.CUSTOMER);
        for (int i = 0; i < n; i++) {
            testData.channelListAdd(channelPojo.getId(), partyPojo.getId());
        }
        Assert.assertEquals(channelListingDto.select(0).size(), n);
    }
}
