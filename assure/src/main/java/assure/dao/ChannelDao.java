package assure.dao;

import assure.pojo.ChannelPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class ChannelDao extends AbstractDao<ChannelPojo> {

    public ChannelPojo selectByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaQuery();
        Root<ChannelPojo> root = criteriaQuery.from(this.clazz);
        criteriaQuery = criteriaQuery.select(root);
        criteriaQuery.where(cb.equal(root.get("name"), name));
        TypedQuery<ChannelPojo> query = em.createQuery(criteriaQuery);
        return getSingle(query);
    }

    public ChannelPojo selectById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaQuery();
        Root<ChannelPojo> root = criteriaQuery.from(this.clazz);
        criteriaQuery = criteriaQuery.select(root);
        criteriaQuery.where(cb.equal(root.get("id"), id));
        TypedQuery<ChannelPojo> query = em.createQuery(criteriaQuery);
        return getSingle(query);
    }
}
