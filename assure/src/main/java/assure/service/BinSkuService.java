package assure.service;

import assure.dao.BinSkuDao;
import assure.pojo.BinSkuPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static assure.util.DataUtil.getKey;
import static assure.util.ValidationUtil.checkDuplicateGlobalSkuAndBinIdPair;
import static java.lang.Math.min;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BinSkuService {
    @Autowired
    private BinSkuDao binSkuDao;

    public List<BinSkuPojo> add(List<BinSkuPojo> binSkuPojoList) throws ApiException {
        checkDuplicateGlobalSkuAndBinIdPair(binSkuPojoList);

        Map<String, BinSkuPojo> globalSkuIdAndBinIdToBinSKuPojo = getGlobalSkuIdAndBinIdToBinSKuPojo(binSkuPojoList);
        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            String key = getKey(Arrays.asList(binSkuPojo.getGlobalSkuId(), binSkuPojo.getBinId()));
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

    public void update(BinSkuPojo binSkuPojo) throws ApiException {
        if (binSkuPojo.getQuantity() < 0L)
            throw new ApiException("Quantity must be greater than 0.");
        BinSkuPojo exists = getCheck(binSkuPojo.getId());
        exists.setQuantity(binSkuPojo.getQuantity());
    }

    public BinSkuPojo getCheck(Long id) throws ApiException {
        BinSkuPojo binSkuPojo = binSkuDao.selectById(id);
        if (isNull(binSkuPojo)) {
            throw new ApiException("id doesn't exist, id : " + id);
        }
        return binSkuPojo;
    }

    public List<BinSkuPojo> select(Integer pageNumber, Integer pageSize) {
        return binSkuDao.select(pageNumber, pageSize);
    }

    public void allocateQty(Long allocateQty, Long globalSkuId) {
        List<BinSkuPojo> binSkuPojoList = binSkuDao.selectByGlobalSkuId(globalSkuId);
        binSkuPojoList.sort(Comparator.comparing(BinSkuPojo::getQuantity));
        Collections.reverse(binSkuPojoList);

        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            Long allocatedQtyInBin = min(allocateQty, binSkuPojo.getQuantity());
            binSkuPojo.setQuantity(binSkuPojo.getQuantity() - allocatedQtyInBin);
            allocateQty -= allocatedQtyInBin;

            if (allocateQty == 0) break;
        }
    }

    private Map<String, BinSkuPojo> getGlobalSkuIdAndBinIdToBinSKuPojo(List<BinSkuPojo> binSkuPojoList) {
        List<BinSkuPojo> exists = binSkuDao.selectForGlobalSkuIdAndBinId(binSkuPojoList);
        return exists.stream().collect(Collectors.
                toMap(binSkuPojo -> getKey(Arrays.asList(binSkuPojo.getGlobalSkuId(), binSkuPojo.getBinId())), binSkuPojo -> binSkuPojo));
    }
}
