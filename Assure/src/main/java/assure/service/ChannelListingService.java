package assure.service;

import assure.dao.ChannelListingDao;
import assure.pojo.ChannelListingPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static assure.util.Helper.validateAddPojoList;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ChannelListingService {
    private static final Long MAX_LIST_SIZE = 1000L;

    @Autowired
    private ChannelListingDao channelListingDao;

    public void add(List<ChannelListingPojo> channelListingPojoList) throws ApiException {
        validateAddPojoList(channelListingPojoList, Arrays.asList("id"), MAX_LIST_SIZE);

        for (ChannelListingPojo channelListingPojo : channelListingPojoList) {
            channelListingDao.add(channelListingPojo);
        }

    }
}
