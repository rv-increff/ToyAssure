package assure.dao;

import assure.pojo.BinSkuPojo;
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
public class BinSkuDao extends AbstractDao<BinSkuPojo> {

     //TODO check the number of calls in where


    public BinSkuPojo selectByBinIdAndGlobalSkuId(Long binId, Long globalSkuIdList) { //Add index
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<BinSkuPojo> root = cr.from(this.clazz);
        cr  = cr.select(root);
        cr.where(cb.equal(root.get("binId"), binId));
        cr.where(cb.equal(root.get("globalSkuIdList"), globalSkuIdList));
        TypedQuery<BinSkuPojo> query =  em.createQuery(cr);
        return getSingle(query);
    }
    public BinSkuPojo selectById(Long id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<BinSkuPojo> root = cr.from(this.clazz);
        cr  = cr.select(root);
        cr.where(cb.equal(root.get("id"), id));
        TypedQuery<BinSkuPojo> query =  em.createQuery(cr);
        return getSingle(query);
    }
}
