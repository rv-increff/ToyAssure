package assure.service;

import assure.dao.ProductDao;
import assure.pojo.ProductPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static assure.util.NormalizeUtil.normalizeProductPojo;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public void add(List<ProductPojo> productPojoList) throws ApiException {
        Long clientId = productPojoList.get(0).getClientId();
        List<ProductPojo> productPojoByClientList = selectByClientId(clientId);
        Set<String> clientSkuIdSet = productPojoByClientList.stream().map(ProductPojo::getClientSkuId)
                .collect(Collectors.toSet());

        for (ProductPojo productPojo : productPojoList) {
            if (clientSkuIdSet.contains(productPojo.getClientSkuId())) {
                throw new ApiException("clientSkuId - clientId pair exists");
            }
        }

        for (ProductPojo productPojo : productPojoList) {
            if (productPojo.getMrp() < 0)
                throw new ApiException("mrp must be greater than 0");
            normalizeProductPojo(productPojo);
            productDao.add(productPojo);
        }
    }

    public List<ProductPojo> select(Integer pageNumber, Integer pageSize) {
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
        if (productPojo.getMrp() < 0)
            throw new ApiException("mrp must be greater than 0");

        if (!Objects.equals(exists.getClientSkuId(), productPojo.getClientSkuId())) {
            if (!isNull(productDao.selectByClientSkuIdAndClientId(productPojo.getClientSkuId(), productPojo.getClientId()))) {
                throw new ApiException("clientSkuId - clientId pair exists");
            }
        }
        exists.setBrandId(productPojo.getBrandId());
        exists.setClientId(productPojo.getClientId());
        exists.setDescription(productPojo.getDescription());
        exists.setMrp(productPojo.getMrp());
        exists.setName(productPojo.getName());

        productDao.update();

    }

    public ProductPojo getCheck(Long id) throws ApiException {
        ProductPojo productPojo = productDao.selectByGlobalSkuId(id);
        if (isNull(productPojo)) {
            throw new ApiException("product with id does not exist");
        }
        return productPojo;
    }

    public ProductPojo selectByClientSkuIdAndClientId(String clientSkuId, Long clientId) {
        return productDao.selectByClientSkuIdAndClientId(clientSkuId, clientId);
    }
    public Map<String, ProductPojo> getClientSkuIdToPojoForClientId(List<String>clientSkuIdList, Long clientId){
        List<ProductPojo> productPojoList = productDao.selectForClientSkuIdAndClientId(clientSkuIdList,clientId);
        return productPojoList.stream().collect(Collectors.toMap(ProductPojo::getClientSkuId, pojo->pojo));
    }

    public ProductPojo selectByGlobalSkuId(Long globalSkuId) {
        return productDao.selectByGlobalSkuId(globalSkuId);
    }

    public Map<Long, ProductPojo> getGlobalSkuIdToPojo(List<Long> globalSkuIdList) {
        List<ProductPojo> productPojoList = productDao.selectForGlobalSkuIdList(globalSkuIdList);
        return productPojoList.stream().collect(Collectors.toMap(ProductPojo::getGlobalSkuId, pojo -> pojo));
    }


    //TODO DEV_REVIEW:this could be handled by static method in ProductService.
    public Map<String, Long> getCheckClientSkuId(List<String> clientSkuIdList, Long clientId) throws ApiException {
        List<ProductPojo> productPojoList = productDao.selectForClientSkuIdAndClientId(clientSkuIdList, clientId);
        Map<String, ProductPojo> clientSkuIdToPojoMap = productPojoList.stream().collect(Collectors.toMap
                (ProductPojo::getClientSkuId, ProductPojo -> ProductPojo));
        Map<String, Long> clientSkuIdToGlobalSkuIdMap = new HashMap<>();

        for (String clientSkuId : clientSkuIdList) {
            ProductPojo productPojo = clientSkuIdToPojoMap.get(clientSkuId);
            if (isNull(productPojo))
                throw new ApiException("Product with clientSkuId does not exists");
            clientSkuIdToGlobalSkuIdMap.put(clientSkuId, productPojo.getGlobalSkuId());
        }
        return clientSkuIdToGlobalSkuIdMap;
    }
}

