package assure.controller;

import assure.dto.BinDto;
import assure.dto.ClientDto;
import assure.model.BinData;
import assure.model.ClientData;
import assure.model.ProductData;
import assure.spring.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class BinController {
    @Autowired
    private BinDto binDto;

    @ApiOperation(value = "Create N bins")
    @RequestMapping(path = "/bins/{numberOfBins}", method = RequestMethod.POST)
    public List<BinData> addBins(@PathVariable Integer numberOfBins) throws ApiException {
        return binDto.add(numberOfBins);
    }

    @ApiOperation(value = "Gives product data")
    @RequestMapping(path = "/bins/pages/{pageNumber}", method = RequestMethod.GET)
    public List<BinData> getBins(@PathVariable Integer pageNumber) {
        return binDto.select(pageNumber);
    }
}
