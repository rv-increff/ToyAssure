package assure.dao;

import assure.pojo.ClientPojo;
import assure.pojo.ProductPojo;
import javafx.util.Pair;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class ClientDao extends AbstractDao {

    public ClientPojo selectById(Long id) {
        List<Pair> where = new ArrayList<>();
        where.add(new Pair("id",id));
        return getSingle(select(ClientPojo.class, where));
    }

    public List<ClientPojo> select(Integer pageNumber, Integer pageSize) {
        return select(ClientPojo.class,pageNumber,pageSize);
    }

}
