package assure.service;

import assure.dao.InventoryDao;
import assure.pojo.InventoryPojo;
import assure.pojo.OrderItemPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public InventoryPojo selectByGlobalSkuId(Long globalSkuId){
        return inventoryDao.selectByGlobalSkuId(globalSkuId);
    }

    public void allocateQty(Long allocateQty,Long globalSkuId) throws ApiException {
        InventoryPojo inventoryPojo = getCheckByGlobalSkuId(globalSkuId);
        inventoryPojo.setAvailableQuantity(inventoryPojo.getAvailableQuantity() - allocateQty);
        inventoryPojo.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity() + allocateQty);
        inventoryDao.update();
    }
    public void fulfillQty(Long fulfilledQty,Long globalSkuId) throws ApiException {
        InventoryPojo inventoryPojo = getCheckByGlobalSkuId(globalSkuId);
        inventoryPojo.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity() - fulfilledQty);
        inventoryPojo.setFulfilledQuantity(fulfilledQty + inventoryPojo.getFulfilledQuantity() );
        inventoryDao.update();
    }


    public InventoryPojo getCheckByGlobalSkuId(Long globalSkuId) throws ApiException {
        InventoryPojo inventoryPojo = selectByGlobalSkuId(globalSkuId);
        if(isNull(inventoryPojo)){
            throw new ApiException("Inventory with global sku Id doesn't exists ");
        }
        return inventoryPojo;
    }
    public Map<OrderItemPojo, InventoryPojo> getOrderItemPojoInvQtyMap(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        Map<OrderItemPojo, InventoryPojo> orderItemPojoInvQtyMap = new HashMap<>();
        Integer row = 1;
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            InventoryPojo inventoryPojo = selectByGlobalSkuId(orderItemPojo.getGlobalSkuId());
            if (isNull(inventoryPojo)) {
                throw new ApiException("Inventory for orderItem does not exists, row : " + row);
            } else {
                orderItemPojoInvQtyMap.put(orderItemPojo, inventoryPojo);
            }
            row++;
        }

        return orderItemPojoInvQtyMap;
    }
}
