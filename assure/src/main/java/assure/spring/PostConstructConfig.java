package assure.spring;

import assure.pojo.ChannelPojo;
import assure.service.ChannelService;
import assure.util.InvoiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static java.util.Objects.isNull;

@Component
public class PostConstructConfig {
    private static final String INTERNAL_CHANNEL_NAME = "INTERNAL";
    @Autowired
    private ChannelService channelService;

    @PostConstruct
    private void postConstruct() {
        checkAndCreateInternalChannel();
    }

    private void checkAndCreateInternalChannel() {
        ChannelPojo channelPojo = channelService.selectByName(INTERNAL_CHANNEL_NAME);
        if(!isNull(channelPojo))           //TODO cyclomatic complexity
            return ;

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
