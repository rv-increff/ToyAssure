package assure.controller;

import assure.dto.ChannelDto;
import assure.model.ChannelData;
import assure.model.ChannelForm;
import assure.spring.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class ChannelController {

    @Autowired
    private ChannelDto channelDto;

    @ApiOperation(value = "Get Channels")  //TODO create default internal channel
    @RequestMapping(path = "/channels", method = RequestMethod.GET)
    public List<ChannelData> getChannels() {
        return channelDto.select();
    }

    @ApiOperation(value = "Add Channel")
    @RequestMapping(path = "/channels", method = RequestMethod.POST)
    public ChannelForm addChannel(@RequestBody ChannelForm channelForm) throws ApiException {
        return channelDto.add(channelForm);
    }

}
