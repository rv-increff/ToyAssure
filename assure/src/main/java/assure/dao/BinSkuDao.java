package assure.dao;

import assure.pojo.BinSkuPojo;
import assure.pojo.ChannelListingPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class BinSkuDao extends AbstractDao<BinSkuPojo> {

    //TODO check the number of calls in where


    public BinSkuPojo selectByGlobalSkuIdAndBinId(Long globalSkuId,Long binId) { //Add index
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<BinSkuPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.and(cb.equal(root.get("globalSkuId"), globalSkuId), cb.equal(root.get("binId"), binId)));
        TypedQuery<BinSkuPojo> query = em.createQuery(cr);
        return getSingle(query);
    }

    public BinSkuPojo selectById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<BinSkuPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.equal(root.get("id"), id));
        TypedQuery<BinSkuPojo> query = em.createQuery(cr);
        return getSingle(query);
    }
    public List<BinSkuPojo> selectByGlobalSkuId(Long globalSkuId) { //Add index
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<BinSkuPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.equal(root.get("globalSkuId"), globalSkuId));
        TypedQuery<BinSkuPojo> query = em.createQuery(cr);
        return query.getResultList();
    }

    public List<BinSkuPojo> selectForBinIds(List<Long> binIdList){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<BinSkuPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(root.get("binId").in(binIdList));
        TypedQuery<BinSkuPojo> query = em.createQuery(cr);
        return query.getResultList();
    }

    public List<BinSkuPojo> selectForGlobalSkuIdAndBinId(List<BinSkuPojo>binSkuPojoList){
        String queryBuilder = "SELECT c FROM BinSkuPojo c WHERE (globalSkuId, binId) IN ";
        List<String> dataList = new ArrayList<>();
        int n = binSkuPojoList.size();
        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            List<String> list = Arrays.asList(binSkuPojo.getGlobalSkuId().toString(), binSkuPojo.getBinId().toString());
            dataList.add("("+String.join(",", list) + ")");
        }

        queryBuilder = queryBuilder +"("+ String.join(",", dataList) + ")";
        TypedQuery<BinSkuPojo> query = em.createQuery(queryBuilder, BinSkuPojo.class);
        return query.getResultList();
    }

}
