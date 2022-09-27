package assure.dto;

import assure.dao.BinDao;
import assure.model.BinSkuData;
import assure.model.BinSkuForm;
import assure.model.BinSkuItemForm;
import assure.model.BinSkuUpdateForm;
import assure.pojo.BinPojo;
import assure.pojo.BinSkuPojo;
import assure.pojo.ProductPojo;
import assure.service.*;
import assure.spring.ApiException;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static assure.util.ConversionUtil.*;
import static assure.util.DataUtil.getKey;
import static assure.util.ValidationUtil.validateList;

@Service
public class BinSkuDto {

    private static final Long MAX_LIMIT = 100L; //TODO move all to common file
    private static final Integer PAGE_SIZE = 5;
    @Autowired
    private BinSkuService binSkuService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BinService binService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private PartyService partyService;
    @Autowired
    private BinDao binDao;

    @Transactional(rollbackFor = ApiException.class)
    public Integer add(BinSkuForm binSkuForm) throws ApiException {
        List<BinSkuItemForm> binSkuItemFormList = binSkuForm.getBinSkuItemFormList();
        validateList("BinSku", binSkuItemFormList, MAX_LIMIT);
        checkDuplicateBinSkuAndBinId(binSkuItemFormList);

        Long clientId = binSkuForm.getClientId();
        partyService.getCheck(clientId);
        checkBinIdExists(binSkuItemFormList);

        List<String> clientSkuIdList = binSkuItemFormList.stream().map(BinSkuItemForm::getClientSkuId)
                .collect(Collectors.toList());
        Map<String, Long> clientToGlobalSkuIdMap = productService.getCheckClientSkuId(clientSkuIdList, clientId);

        List<BinSkuPojo> binSkuPojoList = binSkuService.add(convertListBinSkuFormToPojo(binSkuItemFormList, clientToGlobalSkuIdMap));
        inventoryService.add(convertListBinSkuFormToInventoryPojo(binSkuPojoList));
        return binSkuPojoList.size();
    }

    public List<BinSkuData> select(Integer pageNumber) {
        return convertListBinSkuPojoToData(binSkuService.select(pageNumber, PAGE_SIZE));
    }

    @Transactional(rollbackFor = ApiException.class)
    public BinSkuUpdateForm update(BinSkuUpdateForm binSkuUpdateForm, Long id) throws ApiException {
        BinSkuPojo exists = binSkuService.getCheck(id);
        Long existsQty = exists.getQuantity();
        Long globalSkuId = exists.getGlobalSkuId();

        binSkuService.update(convertBinSkuUpdateFormToPojo(binSkuUpdateForm, id));

        if(existsQty < binSkuUpdateForm.getQuantity())
            inventoryService.increaseInventory(globalSkuId, binSkuUpdateForm.getQuantity() - existsQty);
        else
            inventoryService.decreaseInventory(globalSkuId, existsQty - binSkuUpdateForm.getQuantity());

        return binSkuUpdateForm;
    }

    private BinSkuData convertBinSkuPojoToData(BinSkuPojo binSkuPojo, String clientSkuId) {
        BinSkuData binSkuData = new BinSkuData();
        binSkuData.setBinId(binSkuPojo.getBinId());
        binSkuData.setGlobalSkuId(binSkuPojo.getGlobalSkuId());
        binSkuData.setQuantity(binSkuPojo.getQuantity());
        binSkuData.setId(binSkuPojo.getId());
        binSkuData.setClientSkuId(clientSkuId);
        return binSkuData;
    }

    private List<BinSkuData> convertListBinSkuPojoToData(List<BinSkuPojo> binSkuPojoList) {
        List<BinSkuData> binSkuDataList = new ArrayList<>();
        if (binSkuPojoList.size() == 0)
            return binSkuDataList;

        Map<Long, ProductPojo> globalSkuIdToPojo = productService.getGlobalSkuIdToPojo(binSkuPojoList.stream().
                map(BinSkuPojo::getGlobalSkuId).distinct().collect(Collectors.toList()));
        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            binSkuDataList.add(convertBinSkuPojoToData(binSkuPojo,
                    globalSkuIdToPojo.get(binSkuPojo.getGlobalSkuId()).getClientSkuId()));
        }
        return binSkuDataList;
    }
    private void checkBinIdExists(List<BinSkuItemForm> binSkuItemFormList) throws ApiException {
        List<Long> binIds = binSkuItemFormList.stream().map(BinSkuItemForm::getBinId).distinct().collect(Collectors.toList());
        Set<Long> existingBinIds = binDao.selectForBinIds(binIds).stream().map(BinPojo::getBinId)
                .collect(Collectors.toSet());

        for (BinSkuItemForm binSkuItemForm : binSkuItemFormList) {
            if (!existingBinIds.contains(binSkuItemForm.getBinId())) {
                throw new ApiException( "Bin id doesn't exist, binId : " + binSkuItemForm.getBinId());
            }
        }
    }

    private void checkDuplicateBinSkuAndBinId(List<BinSkuItemForm> binSkuItemFormList) throws ApiException {
        if (CollectionUtils.isEmpty(binSkuItemFormList))
            throw new ApiException("Empty form list");

        Set<String> clientSkuIdAndBinId = new HashSet<>();

        for (BinSkuItemForm binSkuItemForm : binSkuItemFormList) {
            String key = getKey(Collections.singletonList(binSkuItemForm.getClientSkuId() + binSkuItemForm.getBinId()));
            if(clientSkuIdAndBinId.contains(key))
                throw new ApiException("Duplicate bin SKU - bin ID pair : " +
                        binSkuItemForm.getClientSkuId()+ " - "  +binSkuItemForm.getBinId());
            clientSkuIdAndBinId.add(key);
        }

    }

}
