package assure.service;

import assure.dao.BinSkuDao;
import assure.model.BinSkuItemForm;
import assure.pojo.BinSkuPojo;
import assure.spring.ApiException;
import commons.model.ErrorData;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static assure.util.ValidationUtil.checkDuplicateGlobalSkuAndBinIdPair;
import static assure.util.ValidationUtil.throwErrorIfNotEmpty;
import static java.lang.Math.min;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BinSkuService {
    @Autowired
    private BinSkuDao binSkuDao;

    public List<BinSkuPojo> add(List<BinSkuPojo> binSkuPojoList) throws ApiException {

        checkBinIdExists(binSkuPojoList);
        checkDuplicateGlobalSkuAndBinIdPair(binSkuPojoList);

        Map<String, BinSkuPojo> globalSkuIdAndBinIdToBinSKuPojo = getGlobalSkuIdAndBinIdToBinSKuPojo(binSkuPojoList);
        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            String key = binSkuPojo.getGlobalSkuId()+"_"+ binSkuPojo.getBinId(); //TODO add get key fn takes 2 para
            BinSkuPojo exists = globalSkuIdAndBinIdToBinSKuPojo.get(key);
            if (isNull(exists)) {
                binSkuDao.add(binSkuPojo);
            } else {
                exists.setQuantity(exists.getQuantity() + binSkuPojo.getQuantity());
                binSkuDao.update();
            }
        }
        return binSkuPojoList;
    }

    public List<BinSkuPojo> selectForBinIds(List<Long> binIdList){
        return binSkuDao.selectForBinIds(binIdList);
    }

    public Pair<Long, Long> update(BinSkuPojo binSkuPojo) throws ApiException {
        if(binSkuPojo.getQuantity()<0L)
            throw new ApiException("Quantity must be greater than 0.");
        BinSkuPojo exists = binSkuDao.selectById(binSkuPojo.getId());
        if (isNull(exists)) {
            throw new ApiException("id doesn't exist, id : " + binSkuPojo.getId());
        }
        Long existsQty = exists.getQuantity();
        exists.setQuantity(binSkuPojo.getQuantity());
        return new Pair<Long, Long>(existsQty,exists.getGlobalSkuId());
    }

    public List<BinSkuPojo> select(Integer pageNumber, Integer pageSize) {
        return binSkuDao.select(pageNumber, pageSize);
    }

    public void allocateQty(Long allocateQty, Long globalSkuId) {
        List<BinSkuPojo> binSkuPojoList = binSkuDao.selectByGlobalSkuId(globalSkuId);
        Collections.sort(binSkuPojoList, Comparator.comparing(BinSkuPojo::getQuantity));
        Collections.reverse(binSkuPojoList);

        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            Long allocatedQtyInBin = min(allocateQty, binSkuPojo.getQuantity());
            binSkuPojo.setQuantity(binSkuPojo.getQuantity() - allocatedQtyInBin);
            allocateQty -= allocatedQtyInBin;

            if (allocateQty == 0) break;
        }
    }
    private Map<String, BinSkuPojo> getGlobalSkuIdAndBinIdToBinSKuPojo(List<BinSkuPojo> binSkuPojoList){
        List<BinSkuPojo> exists = binSkuDao.selectForGlobalSkuIdAndBinId(binSkuPojoList);
        return exists.stream().collect(Collectors.
                toMap(binSkuPojo -> binSkuPojo.getGlobalSkuId()+"_"+ binSkuPojo.getBinId(),binSkuPojo->binSkuPojo));
    }
    //TODO DEV_REVIEW: Fetch once and for all from DB the binIds that exists and collect them in SET and then compare -done
    //moved from dto to service
    private void checkBinIdExists(List<BinSkuPojo> binSkuPojoList) throws ApiException {
        List<Long> binIds = binSkuPojoList.stream().map(BinSkuPojo::getBinId).distinct().collect(Collectors.toList());
        Set<Long> existingBinIds = selectForBinIds(binIds).stream().map(BinSkuPojo::getBinId)
                .collect(Collectors.toSet());

        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            if (!existingBinIds.contains(binSkuPojo.getBinId())) {
                throw new ApiException( "Bin id doesn't exist, binId : " + binSkuPojo.getBinId());
            }
        }
    }

}
