package assure.dao;

import assure.pojo.ProductPojo;
import javafx.util.Pair;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {

    public ProductPojo selectByGlobalSkuId(Long globalSkuId) {
        List<Pair> whereList = new ArrayList<>();
        whereList.add(new Pair("globalSkuId",globalSkuId));
        return getSingle(selectWhere(ProductPojo.class, whereList));
    }

    public List<ProductPojo> select(Integer pageNumber, Integer pageSize) {
        return select(ProductPojo.class, pageNumber, pageSize);
    }

    public List<ProductPojo> selectByClientId(Long clientId){
        List<Pair> where = new ArrayList<>();
        where.add(new Pair("clientId",clientId));
        return selectWhere(ProductPojo.class, where).getResultList();
    }

    public ProductPojo selectByClientSkuIdClientId(String clientSkuId, Long clientId){
        List<Pair> where = new ArrayList<>();
        where.add(new Pair("clientId",clientId));
        where.add(new Pair("clientSkuId",clientSkuId));
        return getSingle(selectWhere(ProductPojo.class, where));
    }

    public List<ProductPojo> selectByClientSkuIdList(List<String> clientSkuIdList){
        return selectIn(ProductPojo.class, clientSkuIdList, "clientSkuId").getResultList();
    }
    public void update(){

    }

}
