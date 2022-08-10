package assure.dao;

import assure.pojo.BinPojo;
import assure.pojo.ClientPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
@Repository
public class BinDao extends AbstractDao {

    public List<BinPojo> select(Integer pageNumber, Integer pageSize) {
        return select(BinPojo.class, pageNumber, pageSize);
    }

    public List<BinPojo> selectLatestCreatedBins(Integer numberOfBins){
        CriteriaQuery<BinPojo> cr = cr(BinPojo.class);
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
}
