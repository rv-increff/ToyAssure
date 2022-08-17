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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static assure.util.Helper.*;
import static java.util.Objects.isNull;

@Service
public class ProductDto {

    private static final Long MAX_BIN_LIMIT = 1000L;
    private static final Integer PAGE_SIZE = 10;
    @Autowired
    private ProductService productService;
    @Autowired
    private PartyService partyService;

    @Transactional(rollbackFor = ApiException.class)
    public Integer add(List<ProductForm> productFormList, Long consumerId) throws ApiException {
        if (productFormList.size() > MAX_BIN_LIMIT) {
            throw new ApiException("List size more than limit, limit : " + MAX_BIN_LIMIT);
        }

        validateList("Product Form", productFormList);
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
        validate(productUpdateForm);
        Long clientId = productService.selectById(globalSkuId).getClientId();
        productService.update(convertProductUpdateFormToPojo(productUpdateForm, globalSkuId, clientId));
        return productUpdateForm;
    }
}
