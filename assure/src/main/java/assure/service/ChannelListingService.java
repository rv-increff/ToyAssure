package assure.service;

import assure.dao.ChannelListingDao;
import assure.pojo.ChannelListingPojo;
import assure.spring.ApiException;
import commons.model.ErrorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static assure.util.NormalizeUtil.normalizeChannelListingPojo;
import static assure.util.ValidationUtil.throwErrorIfNotEmpty;
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
            channelListingDao.add(channelListingPojo);
        }

    }

    public List<ChannelListingPojo> select(Integer pageNumber, Integer pageSize) {
        return channelListingDao.select(pageNumber, pageSize);
    }

    public ChannelListingPojo selectByChannelIdAndClientIdAndChannelSkuId( Long channelId,Long clientId,String channelSkuId ) {

        return channelListingDao.selectByChannelIdAndClientIdAndChannelSkuId(channelId, clientId, channelSkuId.toLowerCase());
    }

    public ChannelListingPojo selectByGlobalSkuIdAndChannelIdAndClientId(Long globalSkuId, Long channelId, Long clientId) {
        return channelListingDao.selectByGlobalSkuIdAndChannelIdAndClientId(globalSkuId, channelId, clientId);
    }

    private void checkDataNotExist(List<ChannelListingPojo> channelListingPojoList) throws ApiException {
        List<ErrorData> errorFormList = new ArrayList<>();
        for (ChannelListingPojo channelListing : channelListingPojoList) {
            normalizeChannelListingPojo(channelListing);
            ChannelListingPojo channelListingPojo = channelListingDao.selectByAllFields(
                    channelListing.getClientId(), channelListing.getChannelId(),
                    channelListing.getChannelSkuId(), channelListing.getGlobalSkuId());//TODO check constraints loop hole
            //(f,ck1,p,1)
            //(f,ck1,p,2)
            //(a,ck1,p,1)
            //(a,ck2,p,2) -> logic correct
            if (!isNull(channelListingPojo)) {
                throw new ApiException("Channel Listing data already exists");
            }//TODO remove row error logic in service

        }
    }


}
