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
    public List<ChannelListingData> getChannelListings(@RequestParam(name = "pageNumber") Integer pageNumber) {
        return channelListingDto.select(pageNumber);
    }


}
