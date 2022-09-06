package assure.dao;

import assure.config.QaConfig;
import assure.pojo.ChannelPojo;
import assure.util.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class ChannelDaoTest extends AbstractTest {

    @Test
    public void addTest() {
        channelAdd();
        channelAdd();
    }

    @Test
    public void selectTest() {
        List<ChannelPojo> channelPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) channelPojoList.add(channelAdd());

        Assert.assertEquals(channelPojoList.size()+1, channelSelect().size());//1 for post construct self
    }

    @Test
    public void selectByIdTest() {
        ChannelPojo channelPojo = channelAdd();
        Assert.assertEquals(channelPojo, channelDao.selectById(channelPojo.getId()));
    }


    @Test
    public void selectByName() {
        ChannelPojo channelPojo = channelAdd();
        Assert.assertEquals(channelPojo, channelDao.selectByName(channelPojo.getName()));
    }


}
