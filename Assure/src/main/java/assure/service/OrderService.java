package assure.service;

import assure.dao.OrderDao;
import assure.pojo.OrderPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderService {

    @Autowired
    OrderDao orderDao;

    public OrderPojo add(OrderPojo orderPojo) throws ApiException {
        return orderDao.add(orderPojo);
    }

    public OrderPojo selectByChannelIdAndChannelOrderId(Long channelId, String channelOrderId) {
        return orderDao.selectByChannelIdAndChannelOrderId(channelId, channelOrderId);
    }
}
