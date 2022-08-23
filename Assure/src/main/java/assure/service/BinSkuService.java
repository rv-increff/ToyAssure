package assure.service;

import assure.dao.BinSkuDao;
import assure.pojo.BinSkuPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.min;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BinSkuService {
    @Autowired
    private BinSkuDao binSkuDao;

    public void add(List<BinSkuPojo> binSkuPojoList) throws ApiException {
        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            BinSkuPojo exists = binSkuDao.selectByGlobalSkuIdAndBinId(binSkuPojo.getBinId(), binSkuPojo.getGlobalSkuId());
            if (isNull(exists)) {
                binSkuDao.add(binSkuPojo);
            } else {
                exists.setQuantity(exists.getQuantity() + binSkuPojo.getQuantity());
                binSkuDao.update();
            }
        }
    }

    public void update(BinSkuPojo binSkuPojo) throws ApiException {
        BinSkuPojo exists = binSkuDao.selectById(binSkuPojo.getId());
        if (isNull(exists)) {
            throw new ApiException("id doesn't exist, id : " + binSkuPojo.getId());
        }
        exists.setQuantity(binSkuPojo.getQuantity());
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
}
