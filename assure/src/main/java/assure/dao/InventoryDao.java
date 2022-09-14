package assure.dao;

import assure.pojo.BinSkuPojo;
import assure.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao<InventoryPojo>{

    public InventoryPojo selectByGlobalSkuId(Long globalSkuId){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaQuery();
        Root<InventoryPojo> root = criteriaQuery.from(this.clazz);
        criteriaQuery = criteriaQuery.select(root);
        criteriaQuery.where(cb.equal(root.get("globalSkuId"), globalSkuId));
        TypedQuery query = em.createQuery(criteriaQuery);
        return getSingle(query);
    }

    public List<InventoryPojo> selectForGlobalSkus(List<Long> globalSkuIdList){
        CriteriaQuery criteriaQuery = criteriaQuery();
        Root root = criteriaQuery.from(this.clazz);
        criteriaQuery = criteriaQuery.select(root);
        criteriaQuery.where(root.get("globalSkuId").in(globalSkuIdList));
        TypedQuery query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
