package assure.dao;

import assure.pojo.OrderItemPojo;
import assure.util.InvoiceType;
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
        CriteriaQuery cr = cr();
        Root<OrderItemPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.equal(root.get("orderId"), orderId));
        TypedQuery<OrderItemPojo> query = em.createQuery(cr);
        return query.getResultList();
    }





}
