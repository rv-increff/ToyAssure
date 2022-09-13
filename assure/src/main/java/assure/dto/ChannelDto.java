package assure.dto;

import assure.model.ChannelData;
import assure.model.ChannelForm;
import assure.service.ChannelService;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static assure.util.ConversionUtil.convertChannelFormToPojo;
import static assure.util.ConversionUtil.convertChannelPojoListToData;
import static assure.util.ValidationUtil.validateForm;

@Service
public class ChannelDto {
    @Autowired
    private ChannelService channelService;


    //TODO DEV_REVIEW: PageNumber is not required. Channel list can never be that big to require a page number. Do not overoptimise.
    //done
    public List<ChannelData> select() {
        return convertChannelPojoListToData(channelService.select());
    }

    public ChannelForm add(ChannelForm channelForm) throws ApiException {
        validateForm(channelForm);
        channelService.add(convertChannelFormToPojo(channelForm));
        return channelForm;
    }




}
