package assure.dto;

import assure.model.ProductData;
import assure.model.ProductForm;
import assure.model.ProductUpdateForm;
import assure.pojo.ProductPojo;
import assure.service.ConsumerService;
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

    @Autowired
    private ProductService productService;
    @Autowired
    private ConsumerService consumerService;

    @Transactional(rollbackFor = ApiException.class)
    public Integer add(List<ProductForm> productFormList, Long consumerId) throws ApiException {
        Long maxListSize = 1000L;
        if(productFormList.size()>maxListSize){
            throw new ApiException("List size more than limit, limit : " + maxListSize);
        }

        validateList(productFormList);
        checkDuplicateProductsProductForm(productFormList);
        if (isNull(consumerService.selectById(consumerId))) {
            throw new ApiException("client id does not exist");
        }
        productService.add(convertListProductFormToPojo(productFormList, consumerId));
        return productFormList.size();
    }

    public List<ProductData> select(Integer pageNumber) {
        Integer pageSize = 10;
        List<ProductPojo> productPojoList = productService.select(pageNumber, pageSize);
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
