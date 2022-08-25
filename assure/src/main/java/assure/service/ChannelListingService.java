package assure.service;

import assure.dao.ChannelListingDao;
import commons.model.ErrorData;
import assure.pojo.ChannelListingPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
            channelListingDao.add(channelListingPojo);
        }

    }

    public ChannelListingPojo selectByAllFields(Long clientId, Long channelId, String channelSkuId, Long globalSkuId) {
        return channelListingDao.selectByAllFields(clientId, channelId, channelSkuId, globalSkuId);
    }

    private void checkDataNotExist(List<ChannelListingPojo> channelListingPojoList) throws ApiException {
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (ChannelListingPojo channelListing : channelListingPojoList) {
            ChannelListingPojo channelListingPojo = channelListingDao.selectByAllFields(
                    channelListing.getClientId(), channelListing.getChannelId(),
                    channelListing.getChannelSkuId(), channelListing.getGlobalSkuId());//TODO check constraints loop hole
            //(f,ck1,p,1)
            //(f,ck1,p,2)
            //(a,ck1,p,1)
            //(a,ck2,p,2) -> logic correct
            if (!isNull(channelListingPojo)) {
                errorFormList.add(new ErrorData(row, "Channel Listing data already exists"));
            }//TODO remove row error logic in service
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }
}
