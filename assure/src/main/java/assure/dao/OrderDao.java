package assure.dao;

import assure.pojo.OrderItemPojo;
import assure.pojo.OrderPojo;
import assure.pojo.PartyPojo;
import assure.util.InvoiceType;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao<OrderPojo> {

    private static final String SELECT_BY_INVOICE_TYPE = "select o from OrderPojo o where channelId IN " +
            "( select c.id from ChannelPojo c where invoiceType=:invoiceType)";

    public OrderPojo selectByChannelIdAndChannelOrderId(Long channelId, String channelOrderId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<OrderPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.and(
                cb.equal(root.get("channelId"), channelId),
                cb.equal(root.get("channelOrderId"), channelOrderId)
        ));
        TypedQuery<OrderPojo> query = em.createQuery(cr);
        return getSingle(query);
    }

    public OrderPojo selectById(Long id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<OrderPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.equal(root.get("id"), id));
        TypedQuery<OrderPojo> query = em.createQuery(cr);
        return getSingle(query);
    }
    public List<OrderPojo> selectOrderByInvoiceType(Integer pageNumber, Integer pageSize, InvoiceType type) {
        TypedQuery<OrderPojo> query = em.createQuery(SELECT_BY_INVOICE_TYPE, OrderPojo.class);
        query.setParameter("invoiceType",type);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();

    }
}
