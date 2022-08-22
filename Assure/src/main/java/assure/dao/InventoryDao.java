package assure.dao;

import assure.pojo.ChannelPojo;
import assure.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class InventoryDao extends AbstractDao<InventoryPojo>{

    public InventoryPojo selectByGlobalSkuId(Long globalSkuId){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<InventoryPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.equal(root.get("globalSkuId"), globalSkuId));
        TypedQuery<InventoryPojo> query = em.createQuery(cr);
        return getSingle(query);
    }
}
