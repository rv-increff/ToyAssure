package assure.controller;

import assure.dto.ChannelListingDto;
import assure.model.ChannelListingData;
import assure.model.ChannelListingUploadForm;
import assure.spring.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.isNull;

@Api
@RestController
public class ChannelListingController {

    @Autowired
    private ChannelListingDto channelListingDto;

    @ApiOperation(value = "Add ChannelListings")
    @RequestMapping(path = "/channel-listings", method = RequestMethod.POST)
    public Integer addChannelListings(@RequestBody ChannelListingUploadForm channelListingUploadForm) throws ApiException {
        return channelListingDto.add(channelListingUploadForm);
    }

    @ApiOperation(value = "Get ChannelListings")
    @RequestMapping(path = "/channel-listings", method = RequestMethod.GET)
    public List<ChannelListingData> getChannelListings(@RequestParam(required = false) Integer pageNumber,
                                                       @RequestParam(required = false) Long channelId,
                                                       @RequestParam(required = false) Long clientId ) throws ApiException {
        if(!isNull(pageNumber))
            return channelListingDto.select(pageNumber);

        return channelListingDto.selectByChannelIdAndClientId(channelId,clientId);

    }

//    @ApiOperation(value = "Get ChannelListings By ChannelId And ClientId")
//    @RequestMapping(path = "/channel-listings", method = RequestMethod.GET)
//    public List<ChannelListingData> getChannelListingByChannelIdAndClientId(@RequestParam Long channelId, @RequestParam Long clientId ) {
//
//    }


}
