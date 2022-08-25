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
        CriteriaQuery cr = cr();
        Root<BinPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.equal(root.get("binId"), binId));
        TypedQuery<BinPojo> query = em.createQuery(cr);
        return getSingle(query);
    }
}
