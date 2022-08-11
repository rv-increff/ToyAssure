package assure.services;

import assure.dao.BinSkuDao;
import assure.pojo.BinSkuPojo;
import assure.spring.ApiException;
import com.google.common.collect.Iterables;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BinSkuServices {

    @Autowired
    private BinSkuDao dao;

    public void add(List<BinSkuPojo> binSkuPojoList) {
        //TODO get the set from below decide if it exist and the update or create;
        Set<Pair> binIdGlobalSkuIdSet = getExistingBinIdSkuIdSet(binSkuPojoList);
        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            Pair<Long, Long> pair = new Pair<>(binSkuPojo.getBinId(), binSkuPojo.getGlobalSkuId());
            if (binIdGlobalSkuIdSet.contains(pair)) {
//                update();
            } else {
//                add
            }
        }
    }

    private Set<Pair> getExistingBinIdSkuIdSet(List<BinSkuPojo> binSkuPojoList) {
        Integer batchSize = 5;

        List<Pair> binIdGlobalSkuIdSet = binSkuPojoList.stream().map(i -> new Pair(i.getBinId(), i.getGlobalSkuId()))
                .collect(Collectors.toList());
        Iterator<List<Pair>> itr = Iterables.partition(binIdGlobalSkuIdSet, (binIdGlobalSkuIdSet.size()) / batchSize)
                .iterator();
        while (itr.hasNext()) {
            List<Pair> binIdGlobalSkuIdBatch = new ArrayList<>(itr.next());

        }
    }

}
