package assure.service;

import assure.dao.PartyDao;
import commons.model.ErrorData;
import assure.pojo.PartyPojo;
import assure.spring.ApiException;
import assure.util.PartyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static assure.util.NormalizeUtil.normalizePartyPojo;
import static assure.util.ValidationUtil.throwErrorIfNotEmpty;
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
            normalizePartyPojo(partyPojo);
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

    public PartyPojo selectById(Long id) throws ApiException {
        return getCheck(id);
    }

    public PartyPojo getCheck(Long id) throws ApiException {
        PartyPojo partyPojo = dao.selectById(id);
        if (isNull(partyPojo)) {
            throw new ApiException("Party does not exist");
        }
        return partyPojo;
    }
    public Long checkByIdAndType(Long id, PartyType type) throws ApiException {
        PartyPojo partyPojo = dao.selectById(id);
        if (isNull(partyPojo)) {
            throw new ApiException("Party does not exist for given id");
        }
        if(partyPojo.getType()!=type){
            throw new ApiException(type.toString() + " does not exist");
        }
        return id;
    }

    public List<PartyPojo> select(Integer pageNumber, Integer pageSize) {
        return dao.select(pageNumber, pageSize);
    }

}
