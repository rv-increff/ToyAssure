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
        CriteriaQuery criteriaQuery = criteriaQuery();
        Root<ProductPojo> root = criteriaQuery.from(this.clazz);
        criteriaQuery = criteriaQuery.select(root);
        criteriaQuery.where(cb.equal(root.get("globalSkuId"), globalSkuId));
        TypedQuery<ProductPojo> query = em.createQuery(criteriaQuery);
        return getSingle(query);
    }

    public List<ProductPojo> selectByClientId(Long clientId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaQuery();
        Root<ProductPojo> root = criteriaQuery.from(this.clazz);
        criteriaQuery = criteriaQuery.select(root);
        criteriaQuery.where(cb.equal(root.get("clientId"), clientId));
        TypedQuery<ProductPojo> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public ProductPojo selectByClientSkuIdAndClientId(String clientSkuId, Long clientId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaQuery();
        Root<ProductPojo> root = criteriaQuery.from(this.clazz);
        criteriaQuery = criteriaQuery.select(root);
        criteriaQuery.where(cb.and(cb.equal(root.get("clientSkuId"), clientSkuId), cb.equal(root.get("clientId"), clientId)));
        TypedQuery<ProductPojo> query = em.createQuery(criteriaQuery);
        return getSingle(query);
    }
    public List<ProductPojo> selectForGlobalSkuIdList(List<Long>globalSkuIdList){
        CriteriaQuery criteriaQuery = criteriaQuery();
        Root<ProductPojo> root = criteriaQuery.from(this.clazz);
        criteriaQuery = criteriaQuery.select(root);
        criteriaQuery.where(root.get("globalSkuId").in(globalSkuIdList));
        TypedQuery<ProductPojo> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }
    public List<ProductPojo> selectForClientSkuIdAndClientId(List<String>clientSkuIdList, Long clientId){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaQuery();
        Root<ProductPojo> root = criteriaQuery.from(this.clazz);
        criteriaQuery = criteriaQuery.select(root);
        criteriaQuery.where(cb.and(cb.equal(root.get("clientId"), clientId),
                root.get("clientSkuId").in(clientSkuIdList)));
        TypedQuery<ProductPojo> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
