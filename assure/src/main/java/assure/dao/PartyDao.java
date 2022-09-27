package assure.dao;

import assure.pojo.PartyPojo;
import commons.util.PartyType;
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
        CriteriaQuery criteriaQuery = criteriaQuery();
        Root<PartyPojo> root = criteriaQuery.from(this.clazz);
        criteriaQuery = criteriaQuery.select(root);
        criteriaQuery.where(cb.equal(root.get("id"), id));
        TypedQuery<PartyPojo> query = em.createQuery(criteriaQuery);
        return getSingle(query);
    }
     public List<PartyPojo> selectByPartyType(PartyType type) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaQuery();
            Root<PartyPojo> root = criteriaQuery.from(this.clazz);
            criteriaQuery = criteriaQuery.select(root);
            criteriaQuery.where(cb.equal(root.get("type"), type));
            TypedQuery<PartyPojo> query = em.createQuery(criteriaQuery);
            return query.getResultList();
        }
    public List<PartyPojo> selectByPartyType(PartyType type, Integer pageNumber, Integer pageSize) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaQuery();
            Root<PartyPojo> root = criteriaQuery.from(this.clazz);
            criteriaQuery = criteriaQuery.select(root);
            criteriaQuery.where(cb.equal(root.get("type"), type));
            TypedQuery<PartyPojo> query = em.createQuery(criteriaQuery);
            query.setFirstResult(pageNumber * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        }

    public PartyPojo selectByNameAndPartyType(String name, PartyType type) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaQuery();
        Root<PartyPojo> root = criteriaQuery.from(this.clazz);
        criteriaQuery = criteriaQuery.select(root);
        criteriaQuery.where(cb.and((cb.equal(root.get("name"), name)), (cb.equal(root.get("type"), type))));
        TypedQuery<PartyPojo> query = em.createQuery(criteriaQuery);
        return getSingle(query);
    }

    public List<PartyPojo> selectForIdList(List<Long> idList){
        CriteriaQuery criteriaQuery = criteriaQuery();
        Root<PartyPojo> root = criteriaQuery.from(this.clazz);
        criteriaQuery = criteriaQuery.select(root);
        criteriaQuery.where(root.get("id").in(idList));
        TypedQuery<PartyPojo> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
