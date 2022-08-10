package assure.services;

import assure.dao.BinSkuDao;
import assure.dao.ClientDao;
import assure.model.BinSkuForm;
import assure.pojo.BinSkuPojo;
import assure.spring.ApiException;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.Tuple;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BinSkuServices {

    @Autowired
    private BinSkuDao dao;

    public void add(List<BinSkuPojo> binSkuPojoList){
        //TODO get the set from below decide if it exist and the update or create;
        Set<Pair> binIdGlobalSkuIdSet  = getBinIdSkuIdSet(binSkuPojoList);
        for(BinSkuPojo binSkuPojo : binSkuPojoList){
            Pair<Long, Long> pair = new Pair<>(binSkuPojo.getBinId(),binSkuPojo.getGlobalSkuId());
            if(binIdGlobalSkuIdSet.contains(pair)){
//                update();
            }else{
//                add
            }
        }
    }

    private Set<Pair> getBinIdSkuIdSet(List<BinSkuPojo> binSkuPojoList){
        Integer pageNumber = 0;
        Integer pageSize = 5;
        Set<Pair> binSkuBinIdSet = new HashSet<>();
        while(true){
            List<BinSkuPojo> existPojoList = dao.select(pageNumber,pageSize);

            if(CollectionUtils.isEmpty(existPojoList))break;

            Set<Pair> set = new HashSet<>();
            for(BinSkuPojo binSkuPojo : existPojoList){
                Pair<Long, Long> pair = new Pair<>(binSkuPojo.getBinId(),binSkuPojo.getGlobalSkuId());
                set.add(pair);
            }
            Integer row = 0;
            for(BinSkuPojo binSkuPojo : binSkuPojoList){
                Pair<Long, Long> pair = new Pair<>(binSkuPojo.getBinId(),binSkuPojo.getGlobalSkuId());
                if(set.contains(pair)){
                    binSkuBinIdSet.add(pair);
                    binSkuPojoList.remove(row);
                }
                row ++;
            }

            if(CollectionUtils.isEmpty(binSkuPojoList))break;
            pageNumber++;
        }
        return binSkuBinIdSet;
    }
}
