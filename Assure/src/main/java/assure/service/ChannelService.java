package assure.service;

import assure.dao.ChannelDao;
import assure.pojo.ChannelPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class) //TODO add transaction in dto where required
public class ChannelService {
    @Autowired
    private ChannelDao channelDao;

    public List<ChannelPojo> select(Integer pageNumber, Integer pageSize) {
        return channelDao.select(pageNumber, pageSize);
    }
}
