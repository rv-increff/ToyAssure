package assure.dao;

import assure.config.QaConfig;
import assure.pojo.ChannelPojo;
import assure.util.BaseTest;
import assure.util.TestData;
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


public class ChannelDaoTest extends BaseTest {

    @Autowired
    private TestData testData;
    @Autowired
    private ChannelDao channelDao;

    @Test
    public void addTest() {
        testData.channelAdd();
    }

    @Test
    public void selectTest() {
        List<ChannelPojo> channelPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) channelPojoList.add(testData.channelAdd());

        Assert.assertEquals(channelPojoList.size()+1, testData.channelSelect().size());//1 for post construct self
    }

    @Test
    public void selectByIdTest() {
        ChannelPojo channelPojo = testData.channelAdd();
        Assert.assertEquals(channelPojo, channelDao.selectById(channelPojo.getId()));
    }


    @Test
    public void selectByName() {
        ChannelPojo channelPojo = testData.channelAdd();
        Assert.assertEquals(channelPojo, channelDao.selectByName(channelPojo.getName()));
    }


}
