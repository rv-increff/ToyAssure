package assure.controller;

import assure.dto.BinSkuDto;
import assure.model.BinSkuForm;
import assure.model.ProductData;
import assure.spring.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class BinSkuController {

    @Autowired
    private BinSkuDto binSkuDto;

    @ApiOperation(value = "Add binSku")
    @RequestMapping(path = "/binSku", method = RequestMethod.POST)
    public Integer addBinSku(@PathVariable List<BinSkuForm> binSkuFormList) throws ApiException {
        return binSkuDto.add(binSkuFormList);
    }

    @ApiOperation(value = "Gives binSku data")
    @RequestMapping(path = "/binSku/pages/{pageNumber}", method = RequestMethod.GET)
    public List<ProductData> getProducts(@PathVariable Integer pageNumber) {
        return binSkuDto.select(pageNumber);
    }



}
