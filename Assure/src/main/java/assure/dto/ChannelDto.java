package assure.dto;

import assure.model.ChannelData;
import assure.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static assure.util.Helper.convertChannelPojoListToData;

@Service
public class ChannelDto {

    @Autowired
    private ChannelService channelService;

    public List<ChannelData> select(Integer pageNumber){
        Integer pageSize = 10;
        return convertChannelPojoListToData(channelService.select(pageNumber,pageSize));
    }
}
