package assure.dao;

import assure.pojo.ClientPojo;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ClientDao extends AbstractDao {

    public ClientPojo selectById(Long id) {
        return selectById(ClientPojo.class, id);
    }

    public List<ClientPojo> select(Integer pageNumber, Integer pageSize) {
        return select(ClientPojo.class,pageNumber,pageSize);
    }

}
