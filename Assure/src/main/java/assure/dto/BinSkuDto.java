package assure.dto;

import assure.model.*;
import assure.pojo.PartyPojo;
import assure.pojo.ProductPojo;
import assure.service.*;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static assure.util.ConversionUtil.*;
import static assure.util.ConversionUtil.convertListBinSkuFormToPojo;
import static assure.util.DataUtil.checkClientSkuIdExist;
import static assure.util.ValidationUtil.*;
import static java.util.Objects.isNull;

@Service
public class BinSkuDto {

    private static final Long MAX_BIN_LIMIT = 100L;
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

    @Transactional(rollbackFor = ApiException.class)
    public Integer add(BinSkuForm binSkuForm) throws ApiException {
        List<BinSkuItemForm> binSkuFormList = binSkuForm.getBinSkuItemFormList();
        validateList("BinSku", binSkuFormList, MAX_BIN_LIMIT);

        checkBinIdExists(binSkuFormList);
        Long clientId = partyService.getCheck(binSkuForm.getClientId()).getId();
        //TODO make it return map validateAnd
        //TODO db is case insensative normalize clientskudId
        HashMap<String, Long> clientToGlobalSkuIdMap = getClientToGlobalSkuIdMap(binSkuFormList,clientId);
        checkClientSkuIdExist(clientToGlobalSkuIdMap,binSkuFormList);

        binSkuService.add(convertListBinSkuFormToPojo(binSkuFormList, clientToGlobalSkuIdMap));
        inventoryService.add(convertListBinSkuFormToInventoryPojo(binSkuFormList, clientToGlobalSkuIdMap));
        return binSkuFormList.size();
    }

    public List<BinSkuData> select(Integer pageNumber) {
        return convertListBinSkuPojoToData(binSkuService.select(pageNumber, PAGE_SIZE));
    }

    public BinSkuUpdateForm update(BinSkuUpdateForm binSkuUpdateForm, Long id) throws ApiException {
        binSkuService.update(convertBinSkuUpdateFormToPojo(binSkuUpdateForm, id));
        return binSkuUpdateForm;
    }

    private HashMap<String, Long> getClientToGlobalSkuIdMap(List<BinSkuItemForm> binSkuFormList, Long clientId) {
        List<String> clientSkuIdList = binSkuFormList.stream().map(BinSkuItemForm::getClientSkuId)
                .collect(Collectors.toList());

        HashMap<String, Long> clientToGlobalSkuIdMap = new HashMap<>();
        for (String clientSkuId : clientSkuIdList) {
            ProductPojo productPojo = productService.selectByClientSkuIdAndClientId(clientSkuId,clientId);
            if (!isNull(productPojo)) {
                clientToGlobalSkuIdMap.put(productPojo.getClientSkuId(), productPojo.getGlobalSkuId());
            }
        }
        return clientToGlobalSkuIdMap;
    }

    private void checkBinIdExists(List<BinSkuItemForm> binSkuItemFormList) throws ApiException {
        Integer row = 1;
        List<ErrorData> errorFormList = new ArrayList<>();
        for (BinSkuItemForm binSkuForm : binSkuItemFormList) {
            if (isNull(binService.selectById(binSkuForm.getBinId()))) {
                errorFormList.add(new ErrorData(row, "bin id doest not exist, binId : " + binSkuForm.getBinId()));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }
}
