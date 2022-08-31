package assure.dao;

import assure.pojo.ChannelPojo;
import assure.util.InvoiceType;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ChannelDao extends AbstractDao<ChannelPojo> {
    //TODO constructor for adding internal channel;

    public ChannelPojo selectByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<ChannelPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.equal(root.get("name"), name));
        TypedQuery<ChannelPojo> query = em.createQuery(cr);
        return getSingle(query);
    }

    public ChannelPojo selectById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<ChannelPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.equal(root.get("id"), id));
        TypedQuery<ChannelPojo> query = em.createQuery(cr);
        return getSingle(query);
    }
}
