package assure.dto;

import assure.model.ProductData;
import assure.model.ProductForm;
import assure.model.ProductUpdateForm;
import assure.pojo.ProductPojo;
import assure.services.ClientServices;
import assure.services.ProductServices;
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
    private ProductServices productServices;
    @Autowired
    private ClientServices clientServices;

    @Transactional(rollbackFor = ApiException.class)
    public Integer add(List<ProductForm> productFormList, Long clientId) throws ApiException {
        validateList(productFormList);
        checkDuplicateProducts(productFormList);
        if (isNull(clientServices.selectById(clientId))) {
            throw new ApiException("client id does not exist");
        }
        productServices.add(convertListProductFormToPojo(productFormList, clientId));
        return productFormList.size();
    }

    public List<ProductData> select(Integer pageNumber) {
        List<ProductPojo> productPojoList = productServices.select(pageNumber);
        return convertListProductPojoToData(productPojoList);
    }

    public ProductData selectById(Long globalSkuId) throws ApiException {
        ProductPojo productPojo = productServices.selectById(globalSkuId);
        return convertProductPojoToData(productPojo);
    }

    public ProductUpdateForm update(ProductUpdateForm productUpdateForm, Long globalSkuId) throws ApiException {
        validate(productUpdateForm);
        Long clientId = productServices.selectById(globalSkuId).getClientId();
        productServices.update(convertProductUpdateFormToPojo(productUpdateForm, globalSkuId, clientId));
        return productUpdateForm;
    }
}
