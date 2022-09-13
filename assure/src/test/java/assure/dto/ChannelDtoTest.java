package assure.dto;

import assure.config.QaConfig;
import assure.util.BaseTest;
import assure.util.TestData;
import assure.model.ChannelForm;
import assure.pojo.ChannelPojo;
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
