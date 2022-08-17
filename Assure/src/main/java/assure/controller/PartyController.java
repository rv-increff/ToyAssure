package assure.controller;

import assure.dto.PartyDto;
import assure.model.PartyData;
import assure.model.PartyForm;
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
//TODO get similar names for client-customer online synonym
    @ApiOperation(value = "Get Party")  //TODO change name swagger api naming convention
    @RequestMapping(path = "/parties", method = RequestMethod.GET) //TODO 3 modules assure, commons, channels
    public List<PartyData> getParty(@RequestParam(name = "pageNumber") Integer pageNumber) {
        return partyDto.select(pageNumber);
    }
    //TODO check if url correct

    @ApiOperation(value = "Get Party by id")
    @RequestMapping(path = "/parties/{id}", method = RequestMethod.GET)
    public PartyData getPartyById(@PathVariable Long id) {
        return partyDto.selectById(id);
    }

    @ApiOperation(value = "Add party")
    @RequestMapping(path = "/parties", method = RequestMethod.POST)
    public Integer addParty(@RequestBody List<PartyForm> partyFormList) throws ApiException {
        return partyDto.add(partyFormList);
    }


}
