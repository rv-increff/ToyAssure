package assure.service;

import assure.dao.OrderDao;
import assure.dao.OrderItemDao;
import assure.model.ErrorData;
import assure.pojo.OrderItemPojo;
import assure.pojo.OrderPojo;
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
public class OrderService {

    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderItemDao orderItemDao;

    public void add(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojoList) throws ApiException {
        orderDao.add(orderPojo);
        Long orderId = orderPojo.getId();

        checkOrderIdAndGlobalSkuIdPairNotExists(orderItemPojoList);

        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            orderItemPojo.setOrderId(orderId);
            orderItemPojo.setFulfilledQuantity(0L);
            orderItemPojo.setAllocatedQuantity(0L);
            orderItemDao.add(orderItemPojo);
        }
    }

    public OrderPojo selectByChannelIdAndChannelOrderId(Long channelId, String channelOrderId) {
        return orderDao.selectByChannelIdAndChannelOrderId(channelId, channelOrderId);
    }

    private void checkOrderIdAndGlobalSkuIdPairNotExists(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            if (!isNull(orderItemDao.selectByOrderIdAndGlobalSkuID(orderItemPojo.getOrderId(), orderItemPojo.getGlobalSkuId()))) {
                errorFormList.add(new ErrorData(row, "sku already exist for order"));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }
}
