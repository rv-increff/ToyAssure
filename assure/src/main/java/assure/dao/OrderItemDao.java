package assure.dao;

import assure.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao<OrderItemPojo> {

    public List<OrderItemPojo> selectByOrderId(Long orderId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaQuery();
        Root<OrderItemPojo> root = criteriaQuery.from(this.clazz);
        criteriaQuery = criteriaQuery.select(root);
        criteriaQuery.where(cb.equal(root.get("orderId"), orderId));
        TypedQuery<OrderItemPojo> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }





}
