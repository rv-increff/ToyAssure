package assure.dao;

import assure.pojo.PartyPojo;
import assure.util.PartyType;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PartyDao extends AbstractDao<PartyPojo> {
    public PartyPojo selectById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<PartyPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.equal(root.get("id"), id));
        TypedQuery<PartyPojo> query = em.createQuery(cr);
        return getSingle(query);
    }
 public List<PartyPojo> selectByPartyType(PartyType type) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<PartyPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.equal(root.get("type"), type));
        TypedQuery<PartyPojo> query = em.createQuery(cr);
        return query.getResultList();
    }


    public PartyPojo selectByNameAndPartyType(String name, PartyType type) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<PartyPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.and((cb.equal(root.get("name"), name)), (cb.equal(root.get("type"), type))));
        TypedQuery<PartyPojo> query = em.createQuery(cr);
        return getSingle(query);
    }
}
