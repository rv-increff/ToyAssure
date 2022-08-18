package assure.dao;

import assure.pojo.ChannelPojo;
import assure.util.InvoiceType;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class ChannelDao extends AbstractDao<ChannelPojo>{
    public ChannelPojo selectByInvoiceType(InvoiceType invoiceType){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<ChannelPojo> root = cr.from(this.clazz);
        cr  = cr.select(root);
        cr.where(cb.equal(root.get("invoiceType"), invoiceType));
        TypedQuery<ChannelPojo> query =  em.createQuery(cr);
        return getSingle(query);
    }

    public ChannelPojo selectByName(String name){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<ChannelPojo> root = cr.from(this.clazz);
        cr  = cr.select(root);
        cr.where(cb.equal(root.get("name"), name));
        TypedQuery<ChannelPojo> query =  em.createQuery(cr);
        return getSingle(query);
    }
}
