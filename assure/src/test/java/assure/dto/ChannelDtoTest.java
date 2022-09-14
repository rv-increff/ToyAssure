package assure.dto;

import assure.config.BaseTest;
import assure.util.TestData;
import assure.model.ChannelForm;
import assure.pojo.ChannelPojo;
import assure.spring.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class ChannelDtoTest extends BaseTest {

    @Autowired
    private ChannelDto channelDto;
    @Autowired
    private TestData testData;

    @Test
    public void selectTest(){
        ChannelPojo channelPojo = testData.channelAdd();
        Assert.assertEquals(channelPojo.getName(),channelDto.select().get(1).getName());
    }

    @Test(expected = ApiException.class)
    public void addNullErrorTest() throws ApiException {
        ChannelForm channelForm = new ChannelForm();
        channelDto.add(channelForm);
    }

    @Test
    public void addTest() throws ApiException {
        ChannelForm channelForm = testData.getChannelForm();
        Assert.assertEquals(channelForm,channelDto.add(channelForm));
    }
}
