package assure.service;

import assure.dao.ConsumerDao;
import assure.model.ErrorData;
import assure.pojo.ConsumerPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static assure.util.Helper.throwErrorIfNotEmpty;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ConsumerService {

    @Autowired
    private ConsumerDao dao;

    public void add(List<ConsumerPojo> clientPojoList) throws ApiException {
        Integer row = 1;
        List<ErrorData> errorFormList = new ArrayList<>();
        for (ConsumerPojo clientPojo : clientPojoList) {
            if (!isNull(dao.selectByNameAndConsumerType(clientPojo.getName(), clientPojo.getType()))) {
                errorFormList.add(new ErrorData(row, "name - Type pair exists"));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);

        for (ConsumerPojo clientPojo : clientPojoList) {
            dao.add(clientPojo);
        }
    }

    public ConsumerPojo selectById(Long id) {
        return dao.selectById(id);
    }

    public List<ConsumerPojo> select(Integer pageNumber, Integer pageSize) {
        return dao.select(pageNumber, pageSize);
    }

}
