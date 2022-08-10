package assure.dao;

import assure.pojo.ProductPojo;
import javafx.util.Pair;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {

    public ProductPojo selectByGlobalSkuId(Long globalSkuId) {
        List<Pair> whereList = new ArrayList<>();
        whereList.add(new Pair("globalSkuId",globalSkuId));
        return getSingle(select(ProductPojo.class, whereList));
    }

    public List<ProductPojo> select(Integer pageNumber, Integer pageSize) {
        return select(ProductPojo.class, pageNumber, pageSize);
    }

    public List<ProductPojo> selectByClientId(Long clientId){
        List<Pair> where = new ArrayList<>();
        where.add(new Pair("clientId",clientId));
        return select(ProductPojo.class, where).getResultList();
    }

    public ProductPojo selectByClientSkuIdClientId(String clientSkuId, Long clientId){
        List<Pair> where = new ArrayList<>();
        where.add(new Pair("clientId",clientId));
        where.add(new Pair("clientSkuId",clientSkuId));
        return getSingle(select(ProductPojo.class, where));
    }
    public void update(){

    }

}
