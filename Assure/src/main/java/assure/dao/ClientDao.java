package assure.dao;

import assure.pojo.ClientPojo;

import java.util.List;

public class ClientDao extends AbstractDao {

    public ClientPojo selectById(Long id) {
        return selectById(ClientPojo.class, id);
    }

    public List<ClientPojo> select(Integer pageNumber, Integer pageSize) {
        return select(ClientPojo.class,pageNumber,pageSize);
    }

    public ClientPojo selectById(Long id){

    }
}
