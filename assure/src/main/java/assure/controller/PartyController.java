package assure.controller;

import assure.dto.PartyDto;
import commons.model.PartyData;
import assure.model.PartyForm;
import commons.model.PartySearchForm;
import assure.spring.ApiException;
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

    @ApiOperation(value = "Search Party")
    @RequestMapping(path = "/parties/search", method = RequestMethod.POST)
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
