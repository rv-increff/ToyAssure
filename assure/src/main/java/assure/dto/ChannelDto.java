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

    private static final Integer PAGE_SIZE = 10;
    @Autowired
    private ChannelService channelService;


    public List<ChannelData> select(Integer pageNumber) {
        return convertChannelPojoListToData(channelService.select(pageNumber, PAGE_SIZE));
    }

    public ChannelForm add(ChannelForm channelForm) throws ApiException {
        validateForm(channelForm);
        channelService.add(convertChannelFormToPojo(channelForm));
        return channelForm;
    }




}
