package assure.services;

import assure.dao.ProductDao;
import assure.model.ErrorForm;
import assure.pojo.ProductPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static assure.util.Helper.throwErrorIfNotEmpty;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ProductServices {
    @Autowired
    private ProductDao productDao;

    public void add(List<ProductPojo> productPojoList) throws ApiException {

        Long clientId = productPojoList.get(0).getClientId();
        List<ErrorForm> errorFormList = new ArrayList<>();
        List<ProductPojo> productPojoByClientList = selectByClientId(clientId);
        Set<String> clientSkuIdSet = productPojoByClientList.stream().map(ProductPojo::getClientSkuId)
                .collect(Collectors.toSet());
        Integer row = 1;
        for (ProductPojo productPojo : productPojoList) {
            if (clientSkuIdSet.contains(productPojo.getClientSkuId())) {
                errorFormList.add(new ErrorForm(row, "clientSkuId - clientId pair exists"));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);

        for (ProductPojo productPojo : productPojoList) {
            productDao.add(productPojo);
        }
    }

    public List<ProductPojo> select(Integer pageNumber) {
        Integer pageSize = 10;
        return productDao.select(pageNumber, pageSize);
    }

    public ProductPojo selectById(Long globalSkuId) throws ApiException {
        return getCheck(globalSkuId);
    }

    public List<ProductPojo> selectByClientId(Long clientId) {
        return productDao.selectByClientId(clientId);
    }

    public void update(ProductPojo productPojo) throws ApiException {
        ProductPojo exists = getCheck(productPojo.getGlobalSkuId());

        if (!Objects.equals(exists.getClientSkuId(), productPojo.getClientSkuId())) {
            if (!isNull(productDao.selectByClientSkuIdClientId(productPojo.getClientSkuId(), productPojo.getClientId()))) {
                throw new ApiException("clientSkuId - clientId pair exists");
            }
        }

        exists.setBrandId(productPojo.getBrandId());
        exists.setClientId(productPojo.getClientId());
        exists.setDescription(productPojo.getDescription());
        exists.setMrp(productPojo.getMrp());
        exists.setName(productPojo.getName());
        exists.setClientSkuId(productPojo.getClientSkuId());

        productDao.update();

    }

    public ProductPojo getCheck(Long id) throws ApiException {
        ProductPojo productPojo = productDao.selectByGlobalSkuId(id);
        if (isNull(productPojo)) {
            throw new ApiException("product with id does not exist");
        }
        return productPojo;
    }
}

