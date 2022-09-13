package assure.service;

import assure.pojo.ChannelListingPojo;
import assure.spring.ApiException;
import assure.util.BaseTest;
import assure.util.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.fail;

public class ChannelListingServiceTest extends BaseTest {

    @Autowired
    private ChannelListingService channelListingService;
    @Autowired
    private TestData testData;

    @Test
    public void addTest() throws ApiException {
        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();
        int n = 5;
        for (int i = 0; i < n; i++) {
            channelListingPojoList.add(testData.getChannelList());
        }
        channelListingService.add(channelListingPojoList);
        Assert.assertEquals(n, testData.channelListSelect().size());
    }

    @Test
    public void addErrorTest() throws ApiException {
        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();
        channelListingPojoList.add(testData.channelListAdd());
        int n = 5;
        for (int i = 0; i < n; i++) {
            channelListingPojoList.add(testData.getChannelList());
        }
        try {
            channelListingService.add(channelListingPojoList);
            fail("error should be thrown");
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel Listing data already exists");
        }
    }

    @Test
    public void selectTest() {
        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            channelListingPojoList.add(testData.channelListAdd());
        }
        Assert.assertEquals(new HashSet<>(channelListingPojoList), new HashSet<>(channelListingService.select(0, 5)));
    }

    @Test
    public void selectByChannelIdAndClientIdAndChannelSkuIdTest() {
        ChannelListingPojo chLPojo = testData.channelListAdd();
        Assert.assertEquals(chLPojo, channelListingService
                .selectByChannelIdAndClientIdAndChannelSkuId(chLPojo.getChannelId(), chLPojo.getClientId(),
                        chLPojo.getChannelSkuId()));
    }

    @Test
    public void selectByGlobalSkuIdAndChannelIdAndClientIdTest() {
        ChannelListingPojo chLPojo = testData.channelListAdd();
        Assert.assertEquals(chLPojo, channelListingService
                .selectByGlobalSkuIdAndChannelIdAndClientId(chLPojo.getGlobalSkuId(), chLPojo.getChannelId(),
                        chLPojo.getClientId()));
    }


}
