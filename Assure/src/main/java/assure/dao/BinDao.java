package assure.dao;

import assure.pojo.BinPojo;
import assure.pojo.BinSkuPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
@Repository
public class BinDao extends AbstractDao<BinPojo> {


    public List<BinPojo> selectLatestCreatedBins(Integer numberOfBins){
        CriteriaQuery<BinPojo> cr = cr();
        Root<BinPojo> root = cr.from(BinPojo.class);
        List<Order> orderList = new ArrayList();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        orderList.add(builder.desc(root.get("createdAt")));
        cr.select(root).orderBy(orderList);
        TypedQuery<BinPojo> query =  em.createQuery(cr);
        query.setFirstResult(0);
        query.setMaxResults(numberOfBins);

        List<BinPojo> results = query.getResultList();
        return results;
    }
    public List<BinPojo> selectForIds(List<Long> idList){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<BinPojo> root = cr.from(this.clazz);
        cr  = cr.select(root);
        cr.where(root.get("id").in(idList));
        TypedQuery<BinPojo> query =  em.createQuery(cr);
        return query.getResultList();
    }
}
