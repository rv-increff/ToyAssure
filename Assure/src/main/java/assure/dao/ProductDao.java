package assure.dao;

import assure.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao<ProductPojo> {

    public ProductPojo selectByGlobalSkuId(Long globalSkuId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<ProductPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.equal(root.get("globalSkuId"), globalSkuId));
        TypedQuery<ProductPojo> query = em.createQuery(cr);
        return getSingle(query);
    }

    public List<ProductPojo> selectByClientId(Long clientId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<ProductPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.equal(root.get("clientId"), clientId));
        TypedQuery<ProductPojo> query = em.createQuery(cr);
        return query.getResultList();
    }

    public ProductPojo selectByClientIdAndClientSkuId(String clientSkuId, Long clientId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<ProductPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.and(cb.equal(root.get("clientId"), clientId), cb.equal(root.get("clientSkuId"), clientSkuId)));
        TypedQuery<ProductPojo> query = em.createQuery(cr);
        return getSingle(query);
    }

    public ProductPojo selectByClientSkuIdAndClientId(String clientSkuId, Long clientId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<ProductPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.and(cb.equal(root.get("clientSkuId"), clientSkuId), cb.equal(root.get("clientId"), clientId)));
        TypedQuery<ProductPojo> query = em.createQuery(cr);
        return getSingle(query);
    }

    public void update() {

    }

}
