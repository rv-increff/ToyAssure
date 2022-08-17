package assure.service;

import assure.dao.PartyDao;
import assure.model.ErrorData;
import assure.pojo.PartyPojo;
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
public class PartyService {

    @Autowired
    private PartyDao dao;

    public void add(List<PartyPojo> partyPojoList) throws ApiException {
        Integer row = 1;
        List<ErrorData> errorFormList = new ArrayList<>();
        for (PartyPojo partyPojo : partyPojoList) {
            if (!isNull(dao.selectByNameAndPartyType(partyPojo.getName(), partyPojo.getType()))) {
                errorFormList.add(new ErrorData(row, "name - Type pair exists"));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);

        for (PartyPojo clientPojo : partyPojoList) {
            dao.add(clientPojo);
        }
    }

    public PartyPojo selectById(Long id) {
        return dao.selectById(id);
    }

    public List<PartyPojo> select(Integer pageNumber, Integer pageSize) {
        return dao.select(pageNumber, pageSize);
    }

}
