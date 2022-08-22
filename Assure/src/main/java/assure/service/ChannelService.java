package assure.service;

import assure.dao.ChannelDao;
import assure.pojo.ChannelPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static assure.util.NormalizeUtil.normalizeChannelPojo;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class) //TODO add transaction in dto where required
public class ChannelService {
    @Autowired
    private ChannelDao channelDao;


    public List<ChannelPojo> select(Integer pageNumber, Integer pageSize) {
        //TODO post construct not this now manual now cretate post construc class
        return channelDao.select(pageNumber, pageSize);
    }

    public void add(ChannelPojo channelPojo) throws ApiException {
        normalizeChannelPojo(channelPojo);
        if (!isNull(channelDao.selectByName(channelPojo.getName()))) {
            throw new ApiException("Channel already exists");
        }

        channelDao.add(channelPojo);
    }

    public ChannelPojo selectByName(String name) {
        return channelDao.selectByName(name.toUpperCase());
    }

    public void getCheck(Long id) throws ApiException {
        if (isNull(channelDao.selectById(id))) {
            throw new ApiException("channel does not exist");
        }
    }

}
