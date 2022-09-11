package assure.dao;

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
public class ChannelListingDao extends AbstractDao<ChannelListingPojo> {

    public ChannelListingPojo selectByAllFields(Long clientId, Long channelId, String channelSkuId, Long globalSkuId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<ChannelListingPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.and(
                cb.equal(root.get("clientId"), clientId),
                cb.equal(root.get("channelId"), channelId),
                cb.equal(root.get("channelSkuId"), channelSkuId),
                cb.equal(root.get("globalSkuId"), globalSkuId)
        ));
        TypedQuery<ChannelListingPojo> query = em.createQuery(cr);
        return getSingle(query);
    }

    public ChannelListingPojo selectByChannelIdAndClientIdAndChannelSkuId(Long channelId, Long clientId, String channelSkuId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<ChannelListingPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.and(
                cb.equal(root.get("clientId"), clientId),
                cb.equal(root.get("channelId"), channelId),
                cb.equal(root.get("channelSkuId"), channelSkuId)
        ));
        TypedQuery<ChannelListingPojo> query = em.createQuery(cr);
        return getSingle(query);
    }

    public ChannelListingPojo selectByGlobalSkuIdAndChannelIdAndClientId(Long globalSkuId, Long channelId, Long clientId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<ChannelListingPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.and(
                cb.equal(root.get("clientId"), clientId),
                cb.equal(root.get("channelId"), channelId),
                cb.equal(root.get("globalSkuId"), globalSkuId)
        ));
        TypedQuery<ChannelListingPojo> query = em.createQuery(cr);
        return getSingle(query);
    }

    public List<ChannelListingPojo> selectByChannelIdAndClientId(Long channelId, Long clientId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cr();
        Root<ChannelListingPojo> root = cr.from(this.clazz);
        cr = cr.select(root);
        cr.where(cb.and(
                cb.equal(root.get("clientId"), clientId),
                cb.equal(root.get("channelId"), channelId)
        ));
        TypedQuery<ChannelListingPojo> query = em.createQuery(cr);
        return query.getResultList();
    }

    public List<ChannelListingPojo> selectForGlobalSkuIdAndChannelIdAndClientId(List<Long> gSkuList,
                                                                                 Long channelId,
                                                                                 Long clientId){
        String queryBuilder = "SELECT c FROM ChannelListingPojo c WHERE (globalSkuId, channelId, clientId) IN ";
        List<String> dataList = new ArrayList<>();
        int n = gSkuList.size();
        for (int i = 0; i < n; i++) {
            List<String> list = Arrays.asList(gSkuList.get(i).toString(), channelId.toString(), clientId.toString());
            dataList.add("("+String.join(",", list) + ")");
        }
        queryBuilder = queryBuilder +"("+ String.join(",", dataList) + ")";
        TypedQuery<ChannelListingPojo> query = em.createQuery(queryBuilder, ChannelListingPojo.class);
        return query.getResultList();
    }

    public List<ChannelListingPojo> selectForChannelSkuIdAndChannelIdAndClientId(List<String> channelSkuList,
                                                                                 Long channelId,
                                                                                 Long clientId){
        String queryBuilder = "SELECT c FROM ChannelListingPojo c WHERE (globalSkuId, channelSkuId, clientId) IN ";
        List<String> dataList = new ArrayList<>();
        int n = channelSkuList.size();
        for (int i = 0; i < n; i++) {
            List<String> list = Arrays.asList(channelSkuList.get(i).toString(), channelId.toString(), clientId.toString());
            dataList.add("("+String.join(",", list) + ")");
        }
        queryBuilder = queryBuilder +"("+ String.join(",", dataList) + ")";
        TypedQuery<ChannelListingPojo> query = em.createQuery(queryBuilder, ChannelListingPojo.class);
        return query.getResultList();
    }
}
