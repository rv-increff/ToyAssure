package assure.controller;

import assure.dto.PartyDto;
import assure.model.PartyData;
import assure.model.PartyForm;
import assure.model.PartySearchForm;
import assure.spring.ApiException;
import assure.util.PartyType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class PartyController {
    @Autowired
    private PartyDto partyDto;

    @ApiOperation(value = "Get Party")
    @RequestMapping(path = "/parties", method = RequestMethod.GET)
    public List<PartyData> getParty(@RequestParam(name = "pageNumber") Integer pageNumber) {
        return partyDto.select(pageNumber);
    }

    @ApiOperation(value = "Search Party")
    @RequestMapping(path = "/parties/search", method = RequestMethod.POST)
    //TODO club these two send POST request and /party/search
    public List<PartyData> searchParty(@RequestBody PartySearchForm partySearchForm) {
        return partyDto.partySearch(partySearchForm);
    }

    @ApiOperation(value = "Get Party by id")
    @RequestMapping(path = "/parties/{id}", method = RequestMethod.GET)
    public PartyData getPartyById(@PathVariable Long id) throws ApiException {
        return partyDto.selectById(id);
    }

    @ApiOperation(value = "Add party")
    @RequestMapping(path = "/parties", method = RequestMethod.POST)
    public Integer addParty(@RequestBody List<PartyForm> partyFormList) throws ApiException {
        return partyDto.add(partyFormList);
    }
}
