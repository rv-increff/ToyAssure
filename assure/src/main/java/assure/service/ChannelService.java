package assure.service;

import assure.dao.ChannelDao;
import assure.pojo.ChannelPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static assure.util.NormalizeUtil.normalizeChannelPojo;
import static assure.util.NormalizeUtil.normalizeString;
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
        if (!isNull(channelDao.selectByName(channelPojo.getName())))
            throw new ApiException("Channel already exists");

        channelDao.add(channelPojo);
    }

    public ChannelPojo selectByName(String name) throws ApiException {
        return channelDao.selectByName(normalizeString(name));
    }

    public ChannelPojo getCheck(Long id) throws ApiException {
        ChannelPojo channelPojo = channelDao.selectById(id);
        if (isNull(channelPojo))
            throw new ApiException("channel does not exist");

        return channelPojo;
    }

    public Map<Long, ChannelPojo> getCheckChannelIdToPojo(List<Long> channelIdList) throws ApiException {
        List<ChannelPojo> channelPojoList = channelDao.selectForChannelIdList(channelIdList);
        Set<Long> existsChannelId = channelPojoList.stream().map(ChannelPojo::getId).collect(Collectors.toSet());
        for (Long channelId : channelIdList) {
            if(!existsChannelId.contains(channelId))
                throw new ApiException("channel Id "  + channelId + " does not exists");
        }

        return channelPojoList.stream().collect(Collectors.toMap(ChannelPojo::getId, pojo->pojo));
    }

}
