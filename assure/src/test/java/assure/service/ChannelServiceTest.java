package assure.service;

import assure.config.QaConfig;
import assure.util.AbstractTest;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class ChannelServiceTest extends AbstractTest {

    @Autowired
    private ChannelService channelService;

    @Test
    public void selectTest() {
        List<ChannelPojo> channelPojoList = new ArrayList<>();
        channelPojoList.addAll(channelSelect());
        for (int i = 0; i < 5; i++) {
            channelPojoList.add(channelAdd());
        }
        Assert.assertEquals(new HashSet<>(channelPojoList), new HashSet<>(channelService.select()));
    }

    @Test
    public void addTest() throws ApiException {
        ChannelPojo channelPojo = getChannel();
        channelService.add(channelPojo);
    }

    @Test
    public void addErrorTest() {
        ChannelPojo channelPojo = channelAdd();
        try {
            channelService.add(channelPojo);
            fail("error not thrown");
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel already exists");
        }
    }

    @Test
    public void selectByNameTest() {
        ChannelPojo channelPojo = channelAdd();
        Assert.assertEquals(channelPojo, channelService.selectByName(channelPojo.getName()));
    }

    @Test
    public void getCheckTest() throws ApiException {
        ChannelPojo channelPojo = channelAdd();
        Assert.assertEquals(channelPojo, channelService.getCheck(channelPojo.getId()));
    }

    @Test
    public void getCheckErrorTest() throws ApiException {
        ChannelPojo channelPojo = channelAdd();

        try {
            channelService.getCheck(channelPojo.getId() + 1);
            fail("error not thrown");
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "channel does not exist");
        }
    }


}
