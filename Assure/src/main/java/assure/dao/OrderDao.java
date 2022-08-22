package assure.dao;

import assure.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class OrderDao extends AbstractDao<OrderPojo> {
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
}
