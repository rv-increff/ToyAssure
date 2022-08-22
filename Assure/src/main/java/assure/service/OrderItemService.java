package assure.service;

import assure.dao.OrderItemDao;
import assure.model.ErrorData;
import assure.pojo.OrderItemPojo;
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
public class OrderItemService {
    private static final Long MAX_LIST_SIZE = 1000L;

    @Autowired
    OrderItemDao orderItemDao;

    public void add(List<OrderItemPojo> orderItemPojoList) throws ApiException { //TODO move to order
        checkOrderIdAndGlobalSkuIdPairNotExists(orderItemPojoList);

        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            orderItemDao.add(orderItemPojo);
        }

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
