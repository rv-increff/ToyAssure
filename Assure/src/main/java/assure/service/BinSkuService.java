package assure.service;

import assure.dao.BinSkuDao;
import assure.pojo.BinSkuPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static assure.util.Helper.validateList;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BinSkuService {

    @Autowired
    private BinSkuDao dao;

    public void add(List<BinSkuPojo> binSkuPojoList) throws ApiException {
        validateList("Bin sku", binSkuPojoList);
        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            BinSkuPojo exists = dao.selectByBinIdAndGlobalSkuId(binSkuPojo.getBinId(), binSkuPojo.getGlobalSkuId());
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
