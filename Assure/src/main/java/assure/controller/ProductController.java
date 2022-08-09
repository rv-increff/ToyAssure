package assure.controller;

import assure.dto.ProductDto;
import assure.model.ClientForm;
import assure.model.ProductForm;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class ProductController {

    @Autowired
    private ProductDto productDto;

    @ApiOperation(value = "add product")
    @RequestMapping(path = "/products/{clientId}", method = RequestMethod.POST)
    public Integer addProduct(@RequestBody List<ProductForm> productFormList, @PathVariable Long clientId) {
        return productDto.add(productFormList,clientId);
    }

}
