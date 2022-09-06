package assure.dao;

import assure.config.QaConfig;
import assure.pojo.ChannelListingPojo;
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
public class channelListingDaoTest extends AbstractTest {
    @Test
    public void addTest() {
        channelListAdd();
    }

    @Test
    public void selectTest() {
        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) channelListingPojoList.add(channelListAdd());

        Assert.assertEquals(channelListingPojoList, channelListSelect());
    }

    @Test
    public void selectByAllFieldsTest() {
        ChannelListingPojo channelListingPojo = channelListAdd();
        ChannelListingPojo selectPojo = channelListingDao.selectByAllFields(channelListingPojo.getClientId(),
                channelListingPojo.getChannelId(), channelListingPojo.getChannelSkuId(), channelListingPojo.getGlobalSkuId());
        Assert.assertEquals(channelListingPojo, selectPojo);
    }

    @Test
    public void selectByChannelIdAndClientIdAndChannelSkuIdTest() {
        ChannelListingPojo channelListingPojo = channelListAdd();
        ChannelListingPojo selectPojo = channelListingDao.selectByChannelIdAndClientIdAndChannelSkuId(channelListingPojo.getChannelId(),
                channelListingPojo.getClientId(), channelListingPojo.getChannelSkuId());
        Assert.assertEquals(channelListingPojo, selectPojo);
    }

    @Test
    public void selectByGlobalSkuIdAndChannelIdAndClientId() {
        ChannelListingPojo channelListingPojo = channelListAdd();
        ChannelListingPojo selectPojo = channelListingDao.selectByGlobalSkuIdAndChannelIdAndClientId(channelListingPojo.getGlobalSkuId(), channelListingPojo.getChannelId(),
                channelListingPojo.getClientId());
        Assert.assertEquals(channelListingPojo, selectPojo);
    }

}
