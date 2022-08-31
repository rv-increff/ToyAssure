package assure.dao;

import assure.pojo.ChannelListingPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
    public ChannelListingPojo selectByChannelIdAndClientIdAndChannelSkuId( Long channelId, Long clientId,String channelSkuId) {
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

}
