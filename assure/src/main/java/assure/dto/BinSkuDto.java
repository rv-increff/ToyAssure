package assure.dto;

import assure.model.BinSkuData;
import assure.model.BinSkuForm;
import assure.model.BinSkuItemForm;
import assure.model.BinSkuUpdateForm;
import assure.pojo.BinSkuPojo;
import assure.pojo.ProductPojo;
import assure.service.*;
import assure.spring.ApiException;
import commons.model.ErrorData;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static assure.util.ConversionUtil.*;
import static assure.util.DataUtil.checkClientSkuIdExist;
import static assure.util.ValidationUtil.throwErrorIfNotEmpty;
import static assure.util.ValidationUtil.validateList;
import static java.util.Objects.isNull;

@Service
public class BinSkuDto {

    private static final Long MAX_BIN_LIMIT = 100L; //TODO move all to common file
    private static final Integer PAGE_SIZE = 10;
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
//TODO trim strings
    @Transactional(rollbackFor = ApiException.class)
    public Integer add(BinSkuForm binSkuForm) throws ApiException {
        List<BinSkuItemForm> binSkuFormList = binSkuForm.getBinSkuItemFormList();
        validateList("BinSku", binSkuFormList, MAX_BIN_LIMIT);

        checkBinIdExists(binSkuFormList);
        Long clientId = binSkuForm.getClientId();
        partyService.getCheck(clientId);

        HashMap<String, Long> clientToGlobalSkuIdMap = getClientSkuIdToGSkuId(binSkuFormList, clientId);
        checkClientSkuIdExist(clientToGlobalSkuIdMap, binSkuFormList);

        List<BinSkuPojo> binSkuPojoList = binSkuService.add(convertListBinSkuFormToPojo(binSkuFormList, clientToGlobalSkuIdMap));
        inventoryService.add(convertListBinSkuFormToInventoryPojo(binSkuPojoList));
        return binSkuPojoList.size();
    }

    public List<BinSkuData> select(Integer pageNumber) {
        return convertListBinSkuPojoToData(binSkuService.select(pageNumber, PAGE_SIZE));
    }

    @Transactional(rollbackFor = ApiException.class)
    public BinSkuUpdateForm update(BinSkuUpdateForm binSkuUpdateForm, Long id) throws ApiException {
        Pair<Long, Long> dataPair = binSkuService.update(convertBinSkuUpdateFormToPojo(binSkuUpdateForm, id));
        inventoryService.add(convertBinSkuUpdateFormToInventoryPojo(binSkuUpdateForm, dataPair));
        return binSkuUpdateForm;
    }

    //TODO DEV_REVIEW: this should be a public staticmethod in ProductService.
    private HashMap<String, Long> getClientSkuIdToGSkuId(List<BinSkuItemForm> binSkuFormList, Long clientId) {
        List<String> clientSkuIdList = binSkuFormList.stream().map(BinSkuItemForm::getClientSkuId)
                .collect(Collectors.toList());

        HashMap<String, Long> clientToGlobalSkuIdMap = new HashMap<>();
        for (String clientSkuId : clientSkuIdList) {
            ProductPojo productPojo = productService.selectByClientSkuIdAndClientId(clientSkuId, clientId);
            if (!isNull(productPojo)) {
                clientToGlobalSkuIdMap.put(productPojo.getClientSkuId(), productPojo.getGlobalSkuId());
            }
        }
        return clientToGlobalSkuIdMap;
    }

    //TODO DEV_REVIEW: Fetch once and for all from DB the binIds that exists and collect them in SET and then compare
    private void checkBinIdExists(List<BinSkuItemForm> binSkuItemFormList) throws ApiException {
        Integer row = 1;
        List<ErrorData> errorFormList = new ArrayList<>();
        List<Long> binIds = binSkuItemFormList.stream().map(BinSkuItemForm::getBinId).distinct().collect(Collectors.toList());
//        Set<Long> existingBinIds = mewme(binIds);

        for (BinSkuItemForm binSkuForm : binSkuItemFormList) {
            if (isNull(binService.selectById(binSkuForm.getBinId()))) {
                errorFormList.add(new ErrorData(row, "Bin id doesn't exist, binId : " + binSkuForm.getBinId()));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
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
        if (binSkuPojoList.size()==0)
            return binSkuDataList;

        Map<Long, ProductPojo> globalSkuIdToPojo = productService.getGlobalSkuIdToPojo(binSkuPojoList.stream().
                map(BinSkuPojo::getGlobalSkuId).collect(Collectors.toSet()));
        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            binSkuDataList.add(convertBinSkuPojoToData(binSkuPojo,
                    globalSkuIdToPojo.get(binSkuPojo.getGlobalSkuId()).getClientSkuId()));
        }
        return binSkuDataList;
    }
}
