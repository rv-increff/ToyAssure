package assure.dao;

import assure.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {

    public ProductPojo selectById(Long id) {
        return selectById(ProductPojo.class, id);
    }

    public List<ProductPojo> select(Integer pageNumber, Integer pageSize) {
        return select(ProductPojo.class, pageNumber, pageSize);
    }

    public List<ProductPojo> selectByClientSkuIdAndClientId(String clientSkuId, Long clientId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr(ProductPojo.class);
        Root<ProductPojo> root = cr.from(ProductPojo.class);
        cr.select(root).where(cb.equal(root.get("clientSkuId"), clientSkuId)).where(cb.equal(root.get("clientId"), clientId));
        TypedQuery<ProductPojo> query =  em.createQuery(cr);
        return query.getResultList();
    }

    public List<ProductPojo> selectByClientId(Long clientId){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr(ProductPojo.class);
        Root<ProductPojo> root = cr.from(ProductPojo.class);
        cr.select(root).where(cb.equal(root.get("clientId"), clientId));
        TypedQuery<ProductPojo> query =  em.createQuery(cr);
        return query.getResultList();
    }

}
