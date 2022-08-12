package assure.dao;

import assure.pojo.ClientPojo;
import assure.util.Types;
import javafx.util.Pair;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ConsumerDao extends AbstractDao {
    protected final static String SELECT_BY_NAME_TYPE_LIST_BUILDER =
            "select p from ClientPojo p where (name,type) in ";
    public ClientPojo selectById(Long id) {
        List<Pair> where = new ArrayList<>();
        where.add(new Pair("id",id));
        return getSingle(selectWhere(ClientPojo.class, where));
    }

    public List<ClientPojo> select(Integer pageNumber, Integer pageSize) {
        return select(ClientPojo.class,pageNumber,pageSize);
    }

    public List<ClientPojo> selectByNameTypeList(List<Pair<String,Types>> nameTypeList){
        String queryString = SELECT_BY_NAME_TYPE_LIST_BUILDER + "(";
        for (Pair pair : nameTypeList) {
            queryString += "(" + "'"  + pair.getKey() + "'" + ","  + pair.getValue() +  ")";
        }
        queryString += ")";
        TypedQuery<ClientPojo> query = em().createQuery(queryString, ClientPojo.class);
        return query.getResultList();
    }
}
