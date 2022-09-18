package assure.service;

import assure.dao.InventoryDao;
import assure.pojo.InventoryPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class InventoryService {
    @Autowired
    private InventoryDao inventoryDao;

    public static Map<Long, InventoryPojo> getGskuToInventory(List<InventoryPojo> inventoryPojoList) { //TODO ask if right layer
        return inventoryPojoList.stream().collect(Collectors.toMap(InventoryPojo::getGlobalSkuId, inventoryPojo -> inventoryPojo));
    }

    public void add(List<InventoryPojo> inventoryPojoList) throws ApiException {
        for (InventoryPojo inventoryPojo : inventoryPojoList) {
            InventoryPojo exists = inventoryDao.selectByGlobalSkuId(inventoryPojo.getGlobalSkuId());
            if (inventoryPojo.getAvailableQuantity() < 0)
                throw new ApiException("Available quantity should be greater than 0");

            if (isNull(exists)) {
                inventoryPojo.setFulfilledQuantity(0L);
                inventoryPojo.setAllocatedQuantity(0L);
                inventoryDao.add(inventoryPojo);
            } else {
                increaseInventory(exists.getGlobalSkuId(), inventoryPojo.getAvailableQuantity());
            }
        }
    }

    public InventoryPojo selectByGlobalSkuId(Long globalSkuId) {
        return inventoryDao.selectByGlobalSkuId(globalSkuId);
    }

    public void allocateQty(Long allocateQty, Long globalSkuId) throws ApiException {
        InventoryPojo inventoryPojo = getCheckByGlobalSkuId(globalSkuId);
        inventoryPojo.setAvailableQuantity(inventoryPojo.getAvailableQuantity() - allocateQty);
        inventoryPojo.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity() + allocateQty);
        inventoryDao.update();
    }

    public List<InventoryPojo> getAllForGskus(List<Long> globalSkuList) {
        return inventoryDao.selectForGlobalSkus(globalSkuList);
    }

    public void fulfillQty(Long fulfilledQty, Long globalSkuId) throws ApiException {
        InventoryPojo inventoryPojo = getCheckByGlobalSkuId(globalSkuId);
        inventoryPojo.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity() - fulfilledQty);
        inventoryPojo.setFulfilledQuantity(fulfilledQty + inventoryPojo.getFulfilledQuantity());
        inventoryDao.update();
    }

    public InventoryPojo getCheckByGlobalSkuId(Long globalSkuId) throws ApiException {
        InventoryPojo inventoryPojo = selectByGlobalSkuId(globalSkuId);
        if (isNull(inventoryPojo)) {
            throw new ApiException("Inventory with global sku Id doesn't exists ");
        }
        return inventoryPojo;
    }

    public void increaseInventory(Long globalSkuId, Long qty) throws ApiException {
        if (qty < 0)
            throw new ApiException("Inventory increase quantity should be greater than or equal to 0.");

        InventoryPojo exists = getCheckByGlobalSkuId(globalSkuId);
        exists.setAvailableQuantity(exists.getAvailableQuantity() + qty);
        inventoryDao.update();
    }

    public void decreaseInventory(Long globalSkuId, Long qty) throws ApiException {
        if (qty < 0)
            throw new ApiException("Inventory decrease quantity should be greater than or equal to 0.");

        InventoryPojo exists = getCheckByGlobalSkuId(globalSkuId);
        if (exists.getAvailableQuantity() < qty)
            throw new ApiException("Inventory decrease quantity should be less than avalilable quantity");

        exists.setAvailableQuantity(exists.getAvailableQuantity() - qty);
        inventoryDao.update();
    }
}
