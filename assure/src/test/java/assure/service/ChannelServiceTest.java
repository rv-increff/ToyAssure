package assure.service;

import assure.config.BaseTest;
import assure.util.TestData;
import assure.pojo.ChannelPojo;
import assure.spring.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.fail;

public class ChannelServiceTest extends BaseTest {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private TestData testData;

    @Test
    public void selectTest() {
        List<ChannelPojo> channelPojoList = new ArrayList<>();
        channelPojoList.addAll(testData.channelSelect());
        for (int i = 0; i < 5; i++) {
            channelPojoList.add(testData.channelAdd());
        }
        Assert.assertEquals(new HashSet<>(channelPojoList), new HashSet<>(channelService.select()));
    }

    @Test
    public void addTest() throws ApiException {
        ChannelPojo channelPojo = testData.getChannel();
        channelService.add(channelPojo);
    }

    @Test
    public void addErrorTest() {
        ChannelPojo channelPojo = testData.channelAdd();
        try {
            channelService.add(channelPojo);
            fail("error not thrown");
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel already exists");
        }
    }

    @Test
    public void selectByNameTest() {
        ChannelPojo channelPojo = testData.channelAdd();
        Assert.assertEquals(channelPojo, channelService.selectByName(channelPojo.getName()));
    }

    @Test
    public void getCheckTest() throws ApiException {
        ChannelPojo channelPojo = testData.channelAdd();
        Assert.assertEquals(channelPojo, channelService.getCheck(channelPojo.getId()));
    }

    @Test
    public void getCheckErrorTest() throws ApiException {
        ChannelPojo channelPojo = testData.channelAdd();

        try {
            channelService.getCheck(channelPojo.getId() + 1);
            fail("error not thrown");
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "channel does not exist");
        }
    }


}
