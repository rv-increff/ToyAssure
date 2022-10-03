package assure.service;

import assure.dao.PartyDao;
import commons.model.ErrorData;
import assure.pojo.PartyPojo;
import assure.spring.ApiException;
import commons.util.PartyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static assure.util.NormalizeUtil.normalizePartyPojo;
import static assure.util.ValidationUtil.throwErrorIfNotEmpty;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class PartyService {

    @Autowired
    private PartyDao partyDao;

    public void add(List<PartyPojo> partyPojoList) throws ApiException {
        Integer row = 1;
        List<ErrorData> errorFormList = new ArrayList<>();
        for (PartyPojo partyPojo : partyPojoList) {
            normalizePartyPojo(partyPojo);
            if (!isNull(partyDao.selectByNameAndPartyType(partyPojo.getName(), partyPojo.getType()))) {
                errorFormList.add(new ErrorData(row, "name - Type pair exists"));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);

        for (PartyPojo clientPojo : partyPojoList) {
            partyDao.add(clientPojo);
        }
    }

    public PartyPojo selectById(Long id) throws ApiException {
        return getCheck(id);
    }

    public PartyPojo getCheck(Long id) throws ApiException {
        PartyPojo partyPojo = partyDao.selectById(id);
        if (isNull(partyPojo)) {
            throw new ApiException("Party does not exist");
        }
        return partyPojo;
    }
    public Long checkByIdAndType(Long id, PartyType type) throws ApiException {
        PartyPojo partyPojo = partyDao.selectById(id);

        if(isNull(partyPojo) || partyPojo.getType()!=type){
            throw new ApiException(type.toString() + " does not exist");
        }
        return id;
    }

    public List<PartyPojo> select(Integer pageNumber, Integer pageSize) {
        return partyDao.select(pageNumber, pageSize);
    }

    public List<PartyPojo> selectByPartyType(PartyType partyType){
        return partyDao.selectByPartyType(partyType);
    }
    public List<PartyPojo> selectByPartyType(PartyType partyType, Integer pageNumber, Integer pageSize){
        return partyDao.selectByPartyType(partyType, pageNumber, pageSize);
    }

    public Map<Long, PartyPojo> getCheckPartyIdToPojo(List<Long> idList) throws ApiException {
        List<PartyPojo> partyPojoList = partyDao.selectForIdList(idList);
        Set<Long> existsId = partyPojoList.stream().map(PartyPojo::getId).collect(Collectors.toSet());
        for (Long id : idList) {
            if(!existsId.contains(id))
                throw new ApiException("party Id " + id + " does not exists");
        }
        return partyPojoList.stream().collect(Collectors.toMap(PartyPojo::getId, pojo->pojo));
    }

}
