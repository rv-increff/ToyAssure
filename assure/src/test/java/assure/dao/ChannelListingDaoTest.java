package assure.dao;

import assure.pojo.ChannelListingPojo;
import assure.config.BaseTest;
import assure.util.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class ChannelListingDaoTest extends BaseTest {

    @Autowired
    private TestData testData;
    @Autowired
    private ChannelListingDao  channelListingDao;

    @Test
    public void addTest() {
        testData.channelListAdd();
    }

    @Test
    public void selectTest() {
        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) channelListingPojoList.add(testData.channelListAdd());

        Assert.assertEquals(channelListingPojoList, testData.channelListSelect());
    }

    @Test
    public void selectByAllFieldsTest() {
        ChannelListingPojo channelListingPojo = testData.channelListAdd();
        ChannelListingPojo selectPojo = channelListingDao.selectByAllFields(channelListingPojo.getClientId(),
                channelListingPojo.getChannelId(), channelListingPojo.getChannelSkuId());
        Assert.assertEquals(channelListingPojo, selectPojo);
    }

    @Test
    public void selectByChannelIdAndClientIdAndChannelSkuIdTest() {
        ChannelListingPojo channelListingPojo = testData.channelListAdd();
        ChannelListingPojo selectPojo = channelListingDao.selectByChannelIdAndClientIdAndChannelSkuId(channelListingPojo.getChannelId(),
                channelListingPojo.getClientId(), channelListingPojo.getChannelSkuId());
        Assert.assertEquals(channelListingPojo, selectPojo);
    }

    @Test
    public void selectByGlobalSkuIdAndChannelIdAndClientId() {
        ChannelListingPojo channelListingPojo = testData.channelListAdd();
        ChannelListingPojo selectPojo = channelListingDao.selectByGlobalSkuIdAndChannelIdAndClientId(channelListingPojo.getGlobalSkuId(), channelListingPojo.getChannelId(),
                channelListingPojo.getClientId());
        Assert.assertEquals(channelListingPojo, selectPojo);
    }

}
