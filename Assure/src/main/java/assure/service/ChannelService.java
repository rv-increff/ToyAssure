package assure.service;

import assure.dao.ChannelDao;
import assure.pojo.ChannelPojo;
import assure.spring.ApiException;
import assure.util.InvoiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static assure.util.Helper.normalizeChannelPojo;
import static assure.util.Helper.validateAddPojo;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class) //TODO add transaction in dto where required
public class ChannelService {
    @Autowired
    private ChannelDao channelDao;


    public List<ChannelPojo> select(Integer pageNumber, Integer pageSize) {
        checkAndCreateSelf();
        return channelDao.select(pageNumber, pageSize);
    }

    public void add(ChannelPojo channelPojo) throws ApiException {
        validateAddPojo(channelPojo, Arrays.asList("id"));
        normalizeChannelPojo(channelPojo);
        if (!isNull(channelDao.selectByName(channelPojo.getName()))) {
            throw new ApiException("Channel already exists");
        }
        checkAndCreateSelf();

        channelDao.add(channelPojo);
    }

    public ChannelPojo selectByName(String name){
        return channelDao.selectByName(name);
    }

    private void checkAndCreateSelf() {
        ChannelPojo channelPojo = channelDao.selectByInvoiceType(InvoiceType.SELF);
        if (isNull(channelPojo)) {
            channelPojo = new ChannelPojo();
            channelPojo.setName("INTERNAL");
            channelPojo.setInvoiceType(InvoiceType.SELF);
            channelDao.add(channelPojo);
        }
    }
}
