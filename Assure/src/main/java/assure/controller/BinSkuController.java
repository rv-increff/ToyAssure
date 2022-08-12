package assure.controller;

import assure.dto.BinSkuDto;
import assure.model.BinSkuData;
import assure.model.BinSkuForm;
import assure.model.ProductData;
import assure.spring.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class BinSkuController {

    @Autowired
    private BinSkuDto binSkuDto;

    @ApiOperation(value = "Add binSkus")
    @RequestMapping(path = "/bin-skus", method = RequestMethod.POST)
    public Integer addBinSku(@RequestBody List<BinSkuForm> binSkuFormList) throws ApiException {
        return binSkuDto.add(binSkuFormList);
    }

    @ApiOperation(value = "Get binSkus")
    @RequestMapping(path = "/bin-skus", method = RequestMethod.GET)
    public List<BinSkuData> getBinSku(@RequestParam(name = "pageNumber") Integer pageNumber) {
        return binSkuDto.select(pageNumber);
    }



}
