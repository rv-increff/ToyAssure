package assure.service;

import assure.pojo.OrderItemPojo;
import assure.spring.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static assure.util.Helper.validateAddPojoList;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderItemService {

    private static final Long MAX_LIST_SIZE = 1000L;

    public void add(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        validateAddPojoList(orderItemPojoList, Arrays.asList("id","allocatedQuantity","fulfilledQuantity"), MAX_LIST_SIZE);
        checkOrderIdAndGlobalSkuIdPairNotExists()

    }
}
