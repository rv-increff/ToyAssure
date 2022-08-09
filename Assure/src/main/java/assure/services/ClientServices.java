package assure.services;

import assure.dao.ClientDao;
import assure.pojo.ClientPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ClientServices {

    @Autowired
    private ClientDao dao;

    public void add(List<ClientPojo> clientPojoList) {
        for(ClientPojo clientPojo : clientPojoList) {
            dao.add(clientPojo);
        }
    }

    public ClientPojo selectById(Long id) {
        return dao.selectById(id);
    }

    public List<ClientPojo> select(Integer pageNumber, Integer pageSize){
        return dao.select(pageNumber,pageSize);
    }

    public ClientPojo selectByClientId(Long clientId){
        return dao.selectByClientId(clientId);
    }
}
