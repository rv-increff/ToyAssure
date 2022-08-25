package assure.controller;

import assure.dto.BinSkuDto;
import assure.model.BinSkuData;
import assure.model.BinSkuForm;
import assure.model.BinSkuUpdateForm;
import assure.spring.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    public Integer addBinSku(@RequestBody BinSkuForm binSkuForm) throws ApiException {
        return binSkuDto.add(binSkuForm);
    }

    @ApiOperation(value = "Get binSkus")
    @RequestMapping(path = "/bin-skus", method = RequestMethod.GET)
    public List<BinSkuData> getBinSku(@RequestParam(name = "pageNumber") Integer pageNumber) {
        return binSkuDto.select(pageNumber);
    }

    @ApiOperation(value = "Update binSku")
    @RequestMapping(path = "/bin-skus/{id}", method = RequestMethod.PUT) //TODO take them together -> shbham asked to take id here
    public BinSkuUpdateForm updateBinSku(@PathVariable Long id, @RequestBody BinSkuUpdateForm binSkuUpdateForm) throws ApiException {
        return binSkuDto.update(binSkuUpdateForm,id);
    }


}
