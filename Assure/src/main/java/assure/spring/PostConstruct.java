package assure.spring;

import assure.dao.ChannelDao;
import assure.pojo.ChannelPojo;
import assure.util.InvoiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class PostConstruct {
    private static final String INTERNAL_CHANNEL_NAME = "INTERNAL";
    @Autowired
    private ChannelDao channelDao;

    @javax.annotation.PostConstruct
    private void postConstruct() {
        checkAndCreateInternalChannel();
    }

    private void checkAndCreateInternalChannel() {
        ChannelPojo channelPojo = channelDao.selectByInvoiceType(InvoiceType.SELF);
        if (isNull(channelPojo)) {
            channelPojo = new ChannelPojo();
            channelPojo.setName(INTERNAL_CHANNEL_NAME);
            channelPojo.setInvoiceType(InvoiceType.SELF);
            channelDao.add(channelPojo);
        }
    }
}
