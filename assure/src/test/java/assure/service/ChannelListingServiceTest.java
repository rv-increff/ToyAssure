package assure.service;

import assure.config.QaConfig;
import assure.dao.AbstractTest;
import assure.pojo.ChannelListingPojo;
import assure.spring.ApiException;
import io.swagger.annotations.Api;
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
public class ChannelListingServiceTest extends AbstractTest {

    @Autowired
    private ChannelListingService channelListingService;

    @Test
    public void addTest() throws ApiException {
        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();
        int n = 5;
        for (int i = 0; i < n; i++) {
            channelListingPojoList.add(getChannelList());
        }
        channelListingService.add(channelListingPojoList);
        Assert.assertEquals(n, channelListSelect().size());
    }
 @Test
    public void addErrorTest() throws ApiException {
        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();
        channelListingPojoList.add(channelListAdd());
        int n = 5;
        for (int i = 0; i < n; i++) {
            channelListingPojoList.add(getChannelList());
        }
        try{
            channelListingService.add(channelListingPojoList);
            fail("error should be thrown");
        }catch (ApiException e ){
            Assert.assertEquals(e.getMessage(),"Channel Listing data already exists");
        }
    }

    @Test
    public void selectTest(){
        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            channelListingPojoList.add(channelListAdd());
        }
        Assert.assertEquals(new HashSet<>(channelListingPojoList),new  HashSet<>(channelListingService.select(0,5)));
    }

    @Test
    public void selectByChannelIdAndClientIdAndChannelSkuIdTest(){
        ChannelListingPojo chLPojo = channelListAdd();
        Assert.assertEquals(chLPojo, channelListingService
                .selectByChannelIdAndClientIdAndChannelSkuId(chLPojo.getChannelId(), chLPojo.getClientId(),
                        chLPojo.getChannelSkuId()));
    }

    @Test
    public void selectByGlobalSkuIdAndChannelIdAndClientIdTest(){
        ChannelListingPojo chLPojo = channelListAdd();
        Assert.assertEquals(chLPojo, channelListingService
                .selectByGlobalSkuIdAndChannelIdAndClientId(chLPojo.getGlobalSkuId(), chLPojo.getChannelId(),
                        chLPojo.getClientId()));
    }


}
