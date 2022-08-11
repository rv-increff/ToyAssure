package assure.services;

import assure.dao.BinSkuDao;
import assure.pojo.BinSkuPojo;
import assure.spring.ApiException;
import com.google.common.collect.Iterables;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BinSkuServices {

    @Autowired
    private BinSkuDao dao;

    public void add(List<BinSkuPojo> binSkuPojoList) {
        //TODO get the set from below decide if it exist and the update or create;
        Pair<Set<Pair>, List<BinSkuPojo>> dataPair = getExistingBinIdSkuIdSet(binSkuPojoList);
        Set<Pair> existingBinIdGlobalSkuIdSet = dataPair.getKey();
        HashMap<Pair<Long, Long>, Long> incomingDataPairToQtyMap = getIncomingPojoQtyMap(binSkuPojoList);
        List<BinSkuPojo> binSkuPojoListExisting = dataPair.getValue();

        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            Pair<Long, Long> pair = new Pair<>(binSkuPojo.getBinId(), binSkuPojo.getGlobalSkuId());
            if (!existingBinIdGlobalSkuIdSet.contains(pair)) {
                dao.add(binSkuPojo);
            }
        }

        for (BinSkuPojo binSkuPojo : binSkuPojoListExisting) {
            Long incomingQty = incomingDataPairToQtyMap.get(new Pair(binSkuPojo.getBinId(), binSkuPojo.getGlobalSkuId()));
            binSkuPojo.setQuantity(binSkuPojo.getQuantity() + incomingQty);
            dao.update();
        }
    }

    private Pair<Set<Pair>, List<BinSkuPojo>> getExistingBinIdSkuIdSet(List<BinSkuPojo> binSkuPojoList) {
        Integer batchSize = 5;

        Set<Pair> binIdGlobalSkuIdSet = binSkuPojoList.stream().map(i -> new Pair(i.getBinId(), i.getGlobalSkuId()))
                .collect(Collectors.toSet());
        Set<Pair> existingBinIdGlobalSkuIdSet = new HashSet<>();
        List<BinSkuPojo> existingPojoList = new ArrayList<>();

        Iterator<List<Pair>> itr = Iterables.partition(binIdGlobalSkuIdSet, (binIdGlobalSkuIdSet.size()) / batchSize)
                .iterator();

        while (itr.hasNext()) {
            List<Pair> binIdGlobalSkuIdBatch = new ArrayList<>(itr.next());
            List<BinSkuPojo> binSkuPojoFiltered = dao.selectByListBinIdGlobalSkuId(binIdGlobalSkuIdBatch);
            Set<Pair> filteredPojoSet = binSkuPojoFiltered.stream().map(i -> new Pair(i.getBinId(), i.getGlobalSkuId()))
                    .collect(Collectors.toSet());

            existingBinIdGlobalSkuIdSet.addAll(filteredPojoSet);
            existingPojoList.addAll(binSkuPojoFiltered);
        }

        return new Pair(existingBinIdGlobalSkuIdSet, existingPojoList);
    }

    private HashMap<Pair<Long, Long>, Long> getIncomingPojoQtyMap(List<BinSkuPojo> binSkuPojoList) {
        HashMap<Pair<Long, Long>, Long> incomingPairToQtyMap = new HashMap<>();

        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            incomingPairToQtyMap.put(new Pair<>(binSkuPojo.getBinId(), binSkuPojo.getGlobalSkuId()), binSkuPojo.getQuantity());
        }
        return incomingPairToQtyMap;
    }
}
