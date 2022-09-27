package assure.service;

import assure.dao.ChannelListingDao;
import assure.pojo.ChannelListingPojo;
import assure.spring.ApiException;
import commons.model.OrderItemFormChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static assure.util.DataUtil.getKey;
import static assure.util.NormalizeUtil.normalizeChannelListingPojo;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ChannelListingService {


    @Autowired
    private ChannelListingDao channelListingDao;

    public void add(List<ChannelListingPojo> channelListingPojoList) throws ApiException {
        checkDataNotExist(channelListingPojoList);
        for (ChannelListingPojo channelListingPojo : channelListingPojoList) {
            normalizeChannelListingPojo(channelListingPojo);
            if (channelListingPojo.getChannelSkuId().contains(" "))
                throw new ApiException("Channel SKU ID cannot have empty space");
            channelListingDao.add(channelListingPojo);
        }

    }

    public List<ChannelListingPojo> select(Integer pageNumber, Integer pageSize) {
        return channelListingDao.select(pageNumber, pageSize);
    }

    public ChannelListingPojo selectByChannelIdAndClientIdAndChannelSkuId(Long channelId, Long clientId, String channelSkuId) {
        return channelListingDao.selectByChannelIdAndClientIdAndChannelSkuId(channelId, clientId, channelSkuId.toLowerCase());
    }

    public ChannelListingPojo selectByGlobalSkuIdAndChannelIdAndClientId(Long globalSkuId, Long channelId, Long clientId) {
        return channelListingDao.selectByGlobalSkuIdAndChannelIdAndClientId(globalSkuId, channelId, clientId);
    }

    public Map<String, ChannelListingPojo> getGlobalSkuIdAndChannelIdAndClientIdToPojo(List<Long> gSkuList, Long channelId,
                                                                                       Long clientId) {
        List<ChannelListingPojo> channelListingPojoList = channelListingDao.selectForGlobalSkuIdAndChannelIdAndClientId(
                gSkuList, channelId, clientId);
        return channelListingPojoList.stream().collect(Collectors.toMap(pojo -> getKey(Arrays.asList(pojo.getGlobalSkuId(),
                pojo.getChannelId(), pojo.getClientId())), pojo -> pojo));
    }

    public List<ChannelListingPojo> selectByChannelIdAndClientId(Long channelId, Long clientId) {
        return channelListingDao.selectByChannelIdAndClientId(channelId, clientId);
    }

    public Map<String, Long> getCheckChannelSkuId(List<OrderItemFormChannel> orderItemFormChannelList, Long clientId,
                                                  Long channelId) throws ApiException {

        List<String> channelSkuIdList = orderItemFormChannelList.stream().map(OrderItemFormChannel::getChannelSkuId)
                .collect(Collectors.toList());
        List<ChannelListingPojo> channelListingPojoList = channelListingDao.
                selectForChannelSkuIdAndChannelIdAndClientId(channelSkuIdList, channelId, clientId);

        Map<String, Long> channelSkuIdToGlobalSkuId = channelListingPojoList.stream().collect(Collectors.toMap(
                ChannelListingPojo::getChannelSkuId, ChannelListingPojo::getGlobalSkuId));

        for (OrderItemFormChannel orderItemFormChannel : orderItemFormChannelList) {
            if (isNull(channelSkuIdToGlobalSkuId.get(orderItemFormChannel.getChannelSkuId())))
                throw new ApiException("channelSkuID does not exists");
        }
        return channelSkuIdToGlobalSkuId;
    }
    public Map<Long, ChannelListingPojo> getGlobalSkuIdToPojo(List<Long>globalSkuIdList){
        List<ChannelListingPojo> channelListingPojoList = channelListingDao.selectForGlobalSkuId(globalSkuIdList);
        return channelListingPojoList.stream().collect(Collectors.toMap(ChannelListingPojo::getGlobalSkuId, pojo -> pojo));

    }
    private void checkDataNotExist(List<ChannelListingPojo> channelListingPojoList) throws ApiException {

        for (ChannelListingPojo channelListing : channelListingPojoList) {
            //TODO in memory
            normalizeChannelListingPojo(channelListing);
            ChannelListingPojo channelListingPojo = channelListingDao.selectByAllFields(
                    channelListing.getClientId(), channelListing.getChannelId(),
                    channelListing.getChannelSkuId());
            if (!isNull(channelListingPojo)) {
                throw new ApiException("Channel Listing data already exists");
            }

        }
    }


}
