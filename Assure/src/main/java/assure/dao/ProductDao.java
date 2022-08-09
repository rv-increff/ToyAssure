package assure.dao;

import assure.pojo.ProductPojo;

import java.util.List;

public class ProductDao extends AbstractDao {

    public ProductPojo selectById(Long id) {
        return selectById(ProductPojo.class, id);
    }

    public List<ProductPojo> select(Integer pageNumber, Integer pageSize) {
        return select(ProductPojo.class,pageNumber,pageSize);
    }

}
