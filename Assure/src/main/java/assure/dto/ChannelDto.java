package assure.dto;

import assure.model.ChannelData;
import assure.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static assure.util.Helper.convertChannelPojoListToData;

@Service
public class ChannelDto {

    private static final Integer PAGE_SIZE = 10;
    @Autowired
    private ChannelService channelService;

    public List<ChannelData> select(Integer pageNumber) {
        return convertChannelPojoListToData(channelService.select(pageNumber, PAGE_SIZE));
    }
}
