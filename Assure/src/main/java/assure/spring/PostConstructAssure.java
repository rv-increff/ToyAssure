package assure.spring;

import assure.pojo.ChannelPojo;
import assure.service.ChannelService;
import assure.util.InvoiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static java.util.Objects.isNull;

@Component
public class PostConstructAssure {
    private static final String INTERNAL_CHANNEL_NAME = "INTERNAL";
    @Autowired
    private ChannelService channelService;

    @PostConstruct
    private void postConstruct() {
        checkAndCreateInternalChannel();
    }

    private void checkAndCreateInternalChannel() {//TODO call through service
        ChannelPojo channelPojo = channelService.selectByInvoiceType(InvoiceType.SELF);
        if (isNull(channelPojo)) {
            channelPojo = new ChannelPojo();
            channelPojo.setName(INTERNAL_CHANNEL_NAME);
            channelPojo.setInvoiceType(InvoiceType.SELF);
            try {
                channelService.add(channelPojo);
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
