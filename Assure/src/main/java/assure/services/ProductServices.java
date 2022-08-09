package assure.services;

import assure.dao.ProductDao;
import assure.model.ErrorForm;
import assure.pojo.ProductPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static assure.util.Helper.throwErrorIfNotEmpty;

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

    public ProductPojo selectById(Long id) {
        return productDao.selectById(id);
    }

    public List<ProductPojo> selectByClientSkuIdAndClientId(String clientSkuId, Long clientId) {
        return productDao.selectByClientSkuIdAndClientId(clientSkuId, clientId);
    }

    public List<ProductPojo> selectByClientId(Long clientId) {
        return productDao.selectByClientId(clientId);
    }
}

