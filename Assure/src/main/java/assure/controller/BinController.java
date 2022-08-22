package assure.controller;

import assure.dto.BinDto;
import assure.model.BinData;
import assure.spring.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class BinController {
    @Autowired
    private BinDto binDto;

    @ApiOperation(value = "Create N bins")  //TODO RequestParam add check description standard
    @RequestMapping(path = "/bins", method = RequestMethod.POST) //TODO check if request params allowed
    public List<BinData> addBins(@RequestParam(name = "numberOfBins") Integer numberOfBins) throws ApiException {
        return binDto.add(numberOfBins);
    }

    @ApiOperation(value = "Get bins")
    @RequestMapping(path = "/bins", method = RequestMethod.GET)
    public List<BinData> getBins(@RequestParam(name = "pageNumber") Integer pageNumber) {
        return binDto.select(pageNumber);
    }
}
