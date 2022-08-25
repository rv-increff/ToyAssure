package assure.dto;

import assure.model.ProductData;
import assure.model.ProductForm;
import assure.model.ProductUpdateForm;
import assure.pojo.ProductPojo;
import assure.service.PartyService;
import assure.service.ProductService;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static assure.util.ConversionUtil.*;
import static assure.util.ValidationUtil.*;
import static java.util.Objects.isNull;

@Service
public class ProductDto {

    private static final Long MAX_LIST_SIZE = 1000L;
    private static final Integer PAGE_SIZE = 10;
    @Autowired
    private ProductService productService;
    @Autowired
    private PartyService partyService;

    public Integer add(List<ProductForm> productFormList, Long consumerId) throws ApiException {

        validateList("Product Form", productFormList, MAX_LIST_SIZE);
        checkDuplicateProductsProductForm(productFormList);
        if (isNull(partyService.selectById(consumerId))) {
            throw new ApiException("client id does not exist");
        }
        productService.add(convertListProductFormToPojo(productFormList, consumerId));
        return productFormList.size();
    }

    public List<ProductData> select(Integer pageNumber) {
        List<ProductPojo> productPojoList = productService.select(pageNumber, PAGE_SIZE);
        return convertListProductPojoToData(productPojoList);
    }

    public ProductData selectById(Long globalSkuId) throws ApiException {
        ProductPojo productPojo = productService.selectById(globalSkuId);
        return convertProductPojoToData(productPojo);
    }

    public ProductUpdateForm update(ProductUpdateForm productUpdateForm, Long globalSkuId) throws ApiException {
        validateForm(productUpdateForm);
        Long clientId = productService.selectById(globalSkuId).getClientId();
        productService.update(convertProductUpdateFormToPojo(productUpdateForm, globalSkuId, clientId));
        return productUpdateForm;
    }
}
