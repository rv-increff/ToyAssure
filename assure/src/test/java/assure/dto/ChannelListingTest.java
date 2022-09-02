package assure.dto;

import assure.config.QaConfig;
import assure.dao.AbstractTest;
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

import static assure.util.RandomUtil.getRandomString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class ChannelListingTest extends AbstractTest {
    @Autowired
    ChannelListingDto channelListingDto;

    @Test(expected = ApiException.class)
    public void addEmptyErrorTest() throws ApiException {

        ChannelListingUploadForm channelListingUploadForm = new ChannelListingUploadForm();
        channelListingDto.add(channelListingUploadForm);
    }

    @Test
    public void addListSizeTest() {

        ChannelListingUploadForm channelListingUploadForm = new ChannelListingUploadForm();
        List<ChannelListingForm> channelListingFormList = new ArrayList<>();
        for (int i = 0; i < 100001; i++) {
            channelListingFormList.add(new ChannelListingForm());
        }
        channelListingUploadForm.setChannelListingFormList(channelListingFormList);
        try{
            channelListingDto.add(channelListingUploadForm);
        }catch (ApiException e){
            Assert.assertEquals(e.getMessage(),"list size more than max limit, limit : 1000");
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

    @Test
    public void addTest() throws ApiException {
        PartyPojo partyPojo = partyAdd();
        ChannelPojo channelPojo = channelAdd();
        ChannelListingUploadForm channelListingUploadForm = new ChannelListingUploadForm();
        List<ChannelListingForm> channelListingFormList = new ArrayList<>();

        channelListingUploadForm.setChannelId(channelPojo.getId());
        channelListingUploadForm.setClientId(partyPojo.getId());

        for (int i = 0; i < 10; i++) {
            ChannelListingForm channelListingForm = new ChannelListingForm();
            ProductPojo productPojo = productAdd();
            //add products
            channelListingForm.setChannelSkuId(prid);
            channelListingForm.setClientSkuId(getRandomString() + i + 1);
            channelListingFormList.add(channelListingForm);
        }
        channelListingUploadForm.setChannelListingFormList(channelListingFormList);
        channelListingDto.add(channelListingUploadForm);
    }

}
