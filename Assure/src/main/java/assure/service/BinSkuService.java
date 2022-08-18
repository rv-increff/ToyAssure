package assure.service;

import assure.dao.BinSkuDao;
import assure.pojo.BinSkuPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static assure.util.Helper.validateAddPojoList;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BinSkuService {
    private static final Long MAX_LIST_SIZE = 1000L;
    @Autowired
    private BinSkuDao dao;

    public void add(List<BinSkuPojo> binSkuPojoList) throws ApiException {
        validateAddPojoList(binSkuPojoList, Arrays.asList("id"), MAX_LIST_SIZE);

        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            BinSkuPojo exists = dao.selectByGlobalSkuIdAndBinId(binSkuPojo.getBinId(), binSkuPojo.getGlobalSkuId());
            if (isNull(exists)) {
                dao.add(binSkuPojo);
            } else {
                exists.setQuantity(binSkuPojo.getQuantity() + binSkuPojo.getQuantity());
                dao.update();
            }
        }
    }

    public void update(BinSkuPojo binSkuPojo) throws ApiException {
        BinSkuPojo exists = dao.selectById(binSkuPojo.getId());
        if (isNull(exists)) {
            throw new ApiException("id doesn't exist, id : " + binSkuPojo.getId());
        }
        exists.setQuantity(binSkuPojo.getQuantity());
    }

    public List<BinSkuPojo> select(Integer pageNumber, Integer pageSize) {
        return dao.select(pageNumber, pageSize);
    }

}
