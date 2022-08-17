package assure.controller;

import assure.dto.BinSkuDto;
import assure.dto.ChannelDto;
import assure.model.ChannelData;
import assure.model.ConsumerData;
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
    public List<ChannelData> getChannels(@RequestParam(name = "pageNumber") Integer pageNumber) {
        return channelDto.select(pageNumber);
    }

//    @ApiOperation(value = "Add Channel")
//    @RequestMapping(path = "/channels", method = RequestMethod.POST)
//    public ChannelData getChannels(@RequestBody ChannelForm channelForm) {
//        return consumerDto.select(pageNumber);
//    }




}
