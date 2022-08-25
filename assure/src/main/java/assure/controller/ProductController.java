package assure.controller;

import assure.dto.ProductDto;
import assure.model.ProductData;
import assure.model.ProductForm;
import assure.model.ProductUpdateForm;
import assure.spring.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Api
@RestController
public class ProductController {

    @Autowired
    private ProductDto productDto;

    @ApiOperation(value = "Get products")
    @RequestMapping(path = "/products", method = RequestMethod.GET)
    public List<ProductData> getProducts(@RequestParam(name = "pageNumber") Integer pageNumber) {
        return productDto.select(pageNumber);
    }
    @ApiOperation(value = "Get product by id")
    @RequestMapping(path = "/products/{globalSkuId}", method = RequestMethod.GET)
    public ProductData getProductById(@PathVariable Long globalSkuId) throws ApiException {
        return productDto.selectById(globalSkuId);
    }

    @ApiOperation(value = "Add products")
    @RequestMapping(path = "/products", method = RequestMethod.POST)
    public Integer addProducts(@RequestBody List<ProductForm> productFormList, @RequestParam Long partyId) throws ApiException {
        return productDto.add(productFormList, partyId);
    }

    @ApiOperation(value = "Update product")
    @RequestMapping(path = "/products/{globalSkuId}", method = RequestMethod.PUT)
    public ProductUpdateForm updateProduct(@RequestBody ProductUpdateForm productUpdateForm, @PathVariable Long globalSkuId) throws ApiException {
        return productDto.update(productUpdateForm, globalSkuId);
    }
}
