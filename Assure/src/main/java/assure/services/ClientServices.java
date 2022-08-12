package assure.services;

import assure.dao.ConsumerDao;
import assure.model.ErrorForm;
import assure.pojo.ClientPojo;
import assure.spring.ApiException;
import assure.util.DataUtil;
import assure.util.Types;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

import static assure.util.Helper.throwErrorIfNotEmpty;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ClientServices {

    @Autowired
    private ConsumerDao dao;

    public void add(List<ClientPojo> clientPojoList) throws ApiException {
        Set<Pair<String,Types>> existingClientFromIncomingListSet = getExistingClientSet(clientPojoList);
        Integer row = 1;
        List<ErrorForm> errorFormList = new ArrayList<>();
        for (ClientPojo clientPojo : clientPojoList) {
            Pair<String,Types> pair = new Pair(clientPojo.getName(), clientPojo.getType());
            if(existingClientFromIncomingListSet.contains(pair)){
                errorFormList.add(new ErrorForm(row, "name - Type pair exists"));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);

        for(ClientPojo clientPojo : clientPojoList) {
            dao.add(clientPojo);
        }
    }

    public Set<Pair<String,Types>> getExistingClientSet(List<ClientPojo> clientPojoList){

        Integer batchSize = 5;
        List<Pair<String,Types>> nameTypeList = clientPojoList.stream().map(clientPojo -> new Pair<String,Types>(clientPojo.getName(), clientPojo.getType()))
                .collect(Collectors.toList());
        Set<Pair<String,Types>> existingNameTypeSet = new HashSet<>();
        List<List<Pair<String,Types>>> subLists = DataUtil.partition(nameTypeList,(int)Math.ceil((double)nameTypeList.size()/batchSize));
        for (List<Pair<String, Types>> subList : subLists) {
            List<Pair<String,Types>> batchSet = subList;
            List<ClientPojo> clientPojoFiltered = dao.selectByNameTypeList(batchSet);
            Set<Pair<String,Types>> filteredPojoSet = clientPojoFiltered.stream()
                    .map(clientPojo -> new Pair<String,Types>(clientPojo.getName(), clientPojo.getType()))
                    .collect(Collectors.toSet());
            existingNameTypeSet.addAll(filteredPojoSet);
        }
        return existingNameTypeSet;
    }

    public ClientPojo selectById(Long id) {
        return dao.selectById(id);
    }

    public List<ClientPojo> select(Integer pageNumber, Integer pageSize){
        return dao.select(pageNumber,pageSize);
    }

}
