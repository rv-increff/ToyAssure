package assure.dao;

import assure.pojo.BinPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class BinDao extends AbstractDao<BinPojo> {

    public BinPojo selectById(Long binId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaQuery(); //TODO criteriaQuery to criteriaQuery
        Root<BinPojo> root = criteriaQuery.from(this.clazz);
        criteriaQuery = criteriaQuery.select(root);

        criteriaQuery.where(cb.equal(root.get("binId"), binId));
        TypedQuery<BinPojo> query = em.createQuery(criteriaQuery);
        return getSingle(query);
    }
}
