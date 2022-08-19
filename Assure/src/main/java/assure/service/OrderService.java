package assure.service;

import assure.dao.OrderDao;
import assure.pojo.OrderPojo;
import assure.spring.ApiException;
import assure.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static assure.util.Helper.validateAddPojo;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderService {

    @Autowired
    OrderDao orderDao;

    public OrderPojo add(OrderPojo orderPojo) throws ApiException {
        validateAddPojo(orderPojo, Arrays.asList("id"));
        return orderDao.add(orderPojo);
    }
}
