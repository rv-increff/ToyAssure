package assure.service;

import assure.dao.InventoryDao;
import assure.pojo.InventoryPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class InventoryService {
    @Autowired
    private InventoryDao inventoryDao;

    public void add(List<InventoryPojo> inventoryPojoList){
        for (InventoryPojo inventoryPojo : inventoryPojoList) {
            InventoryPojo exists = inventoryDao.selectByGlobalSkuId(inventoryPojo.getGlobalSkuId());
            if(isNull(exists)){
                inventoryPojo.setFulfilledQuantity(0L);
                inventoryPojo.setAllocatedQuantity(0L);
                inventoryDao.add(inventoryPojo);
            }
            else{
                exists.setAvailableQuantity(exists.getAvailableQuantity() + inventoryPojo.getAvailableQuantity());
                inventoryDao.update();
            }
        }
    }
}
