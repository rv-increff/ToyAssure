package assure.service;

import assure.dao.ChannelDao;
import assure.pojo.ChannelPojo;
import assure.spring.ApiException;
import assure.util.InvoiceType;
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


    public List<ChannelPojo> select() {
        return channelDao.select();
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

    public ChannelPojo getCheck(Long id) throws ApiException {
        ChannelPojo channelPojo = channelDao.selectById(id);
        if (isNull(channelPojo)) {
            throw new ApiException("channel does not exist");
        }
        return channelPojo;
    }

}
