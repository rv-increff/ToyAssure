package assure.service;

import assure.config.QaConfig;
import assure.dao.AbstractTest;
import assure.pojo.ChannelListingPojo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class ChannelListingServiceTest extends AbstractTest {

    @Autowired
    private ChannelListingService channelListingService;

    @Test
    public void addTest(){

    }

}
