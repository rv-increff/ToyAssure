package assure.dao;

import assure.pojo.BinSkuPojo;
import javafx.util.Pair;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class BinSkuDao extends AbstractDao {

    protected final static String SELECT_BY_BIND_ID_GLOBAL_SKU_ID_LIST = "";

    public List<BinSkuPojo> select(Integer pageNumber, Integer pageSize) {
        return select(BinSkuPojo.class, pageNumber, pageSize);
    }

    public List<BinSkuPojo> selectByListBinIdGlobalSkuId(List<Pair> binIdGlobalSkuIdList){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr(BinSkuPojo.class);
        Root<BinSkuPojo> root = cr.from(BinSkuPojo.class);
        cr  = cr.select(root).where(root.get(columnName).in(inList));
        TypedQuery<T> query =  em.createQuery(cr);
        return query;
    }
}
