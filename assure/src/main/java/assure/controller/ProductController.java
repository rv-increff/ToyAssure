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
    public List<ProductData> getProducts(@RequestParam Integer pageNumber) {
        return productDto.select(pageNumber);
    }

    @ApiOperation(value = "Get product by id")
    @RequestMapping(path = "/products/{globalSkuId}", method = RequestMethod.GET)
    public ProductData getProductById(@PathVariable Long globalSkuId) throws ApiException {
        return productDto.selectById(globalSkuId);
    }

    @ApiOperation(value = "Add products")
    @RequestMapping(path = "/products", method = RequestMethod.POST)
    public Integer addProducts(@RequestParam Long clientId, @RequestBody List<ProductForm> productFormList)
            throws ApiException {
        return productDto.add(productFormList, clientId);
    }

    @ApiOperation(value = "Update product")
    @RequestMapping(path = "/products/{globalSkuId}", method = RequestMethod.PUT)
    public ProductUpdateForm updateProduct( @PathVariable Long globalSkuId, @RequestBody ProductUpdateForm productUpdateForm)
            throws ApiException {
        return productDto.update(productUpdateForm, globalSkuId);
    }

    @ApiOperation(value = "Get product by client Id")
    @RequestMapping(path = "/products/client-id", method = RequestMethod.GET)
    public List<ProductData> getProductByClientId(@RequestParam Long clientId) throws ApiException {
        return productDto.selectByClientId(clientId);
    }

}
