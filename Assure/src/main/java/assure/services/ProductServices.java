package assure.services;

import assure.dao.ClientDao;
import assure.pojo.ClientPojo;
import assure.pojo.ProductPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ProductServices {
    @Autowired
    private ClientDao dao;

    public void add(List<ProductPojo> productPojoList) {
        for(ProductPojo productPojo : productPojoList) {
            dao.add(productPojo);
        }
    }

}
