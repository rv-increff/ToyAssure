package assure.service;

import assure.dao.ChannelListingDao;
import assure.pojo.ChannelListingPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    public Map<String, Long> getCheckChannelSkuId(List<String> channelSkuIdList, Long clientId,
                                                  Long channelId) throws ApiException {

        Map<String, ChannelListingPojo> channelSkuIdToPojo = getClientSkuIdToPojoForClientIdChannelId(channelSkuIdList,
                clientId, channelId);
        Map<String, Long> channelSkuIdToGlobalSkuId = new HashMap<>();

        for (String channelSkuId : channelSkuIdList) {
            if (isNull(channelSkuIdToPojo.get(channelSkuId)))
                throw new ApiException("Channel SKU ID does not exists");
            channelSkuIdToGlobalSkuId.put(channelSkuId, channelSkuIdToPojo.get(channelSkuId).getGlobalSkuId());
        }

        return channelSkuIdToGlobalSkuId;
    }
    public Map<Long, ChannelListingPojo> getGlobalSkuToPojo(List<Long>globalSkuIdList, Long clientId, Long channelId){
        List<ChannelListingPojo> channelListingPojoList = channelListingDao.selectForGlobalSkuId(globalSkuIdList, clientId, channelId);
        return channelListingPojoList.stream().collect(Collectors.toMap(ChannelListingPojo::getGlobalSkuId, pojo -> pojo));

    }
    private void checkDataNotExist(List<ChannelListingPojo> channelListingPojoList) throws ApiException {
        List<String> channelSkuIdList = channelListingPojoList.stream().map(ChannelListingPojo::getChannelSkuId).
                distinct().collect(Collectors.toList());

        checkPojoListHasSameClientId(channelListingPojoList);
        checkPojoListHasSameChannelId(channelListingPojoList);

        Long clientId = channelListingPojoList.get(0).getClientId();
        Long channelId = channelListingPojoList.get(0).getChannelId();

        Map<String, ChannelListingPojo> channelSkuIdToPojo = getClientSkuIdToPojoForClientIdChannelId(channelSkuIdList,
                clientId, channelId);
        for (ChannelListingPojo channelListing : channelListingPojoList) {
            normalizeChannelListingPojo(channelListing);
            ChannelListingPojo channelListingPojo = channelSkuIdToPojo.get(channelListing.getChannelSkuId());
            if (!isNull(channelListingPojo)) {
                throw new ApiException("Channel Listing data already exists");
            }
        }
    }

    public void checkPojoListHasSameClientId(List<ChannelListingPojo> channelListingPojoList) throws ApiException {
        Set<Long> clientIds = channelListingPojoList.stream().map(ChannelListingPojo::getClientId).collect(Collectors
                .toSet());
        if (clientIds.size() != 1)
            throw new ApiException("All client IDs in the list must match");
    }
    public void checkPojoListHasSameChannelId(List<ChannelListingPojo> channelListingPojoList) throws ApiException {
        Set<Long> channelIds = channelListingPojoList.stream().map(ChannelListingPojo::getChannelId).collect(Collectors
                .toSet());
        if(channelIds.size() != 1)
            throw new ApiException("All channel IDs in the list must match");
    }

    private Map<String, ChannelListingPojo> getClientSkuIdToPojoForClientIdChannelId(List<String> channelSkuIdList,
                                                                                     Long clientId, Long channelId){
    List<ChannelListingPojo> channelListingPojoList = channelListingDao.
            selectForChannelSkuIdAndChannelIdAndClientId(channelSkuIdList, channelId, clientId);

    return channelListingPojoList.stream().collect(Collectors.toMap(
            ChannelListingPojo::getChannelSkuId, channelListingPojo -> channelListingPojo));
    }
}
