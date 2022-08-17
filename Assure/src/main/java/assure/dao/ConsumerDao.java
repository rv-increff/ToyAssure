package assure.dao;

import assure.pojo.ConsumerPojo;
import assure.util.ConsumerTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class ConsumerDao extends AbstractDao<ConsumerPojo> {
    public ConsumerPojo selectById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<ConsumerPojo> root = cr.from(this.clazz);
        cr  = cr.select(root);
        cr.where(cb.equal(root.get("id"), id));
        TypedQuery<ConsumerPojo> query =  em.createQuery(cr);
        return getSingle(query);
    }

    public ConsumerPojo selectByNameAndConsumerType(String name, ConsumerTypes type){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<ConsumerPojo> root = cr.from(this.clazz);
        cr  = cr.select(root);
        cr.where(cb.equal(root.get("name"), name));
        cr.where(cb.equal(root.get("ConsumerTypes"), type));
        TypedQuery<ConsumerPojo> query =  em.createQuery(cr);
        return getSingle(query);
    }
}
