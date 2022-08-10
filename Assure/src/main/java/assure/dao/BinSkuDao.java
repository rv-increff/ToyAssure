package assure.dao;

import assure.pojo.BinSkuPojo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BinSkuDao extends AbstractDao {

    public List<BinSkuPojo> select(Integer pageNumber, Integer pageSize) {
        return select(BinSkuPojo.class, pageNumber, pageSize);
    }
}
