package assure.dto;

import assure.model.BinSkuData;
import assure.model.BinSkuForm;
import assure.model.BinSkuUpdateForm;
import assure.model.ErrorData;
import assure.pojo.ProductPojo;
import assure.service.BinService;
import assure.service.BinSkuService;
import assure.service.InventoryService;
import assure.service.ProductService;
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

    @Transactional(rollbackFor = ApiException.class)
    public Integer add(List<BinSkuForm> binSkuFormList) throws ApiException {

        validateList("BinSku", binSkuFormList, MAX_BIN_LIMIT);
        checkDuplicateProductsBinSkuForm(binSkuFormList);
        checkBinIdExists(binSkuFormList);

        HashMap<String, Long> clientToGlobalSkuIdMap = getClientToGlobalSkuIdMap(binSkuFormList);
        checkClientSkuIdExist(clientToGlobalSkuIdMap, binSkuFormList); //TODO make it return map validateAnd

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

    private HashMap<String, Long> getClientToGlobalSkuIdMap(List<BinSkuForm> binSkuFormList) {
        List<String> clientSkuIdList = binSkuFormList.stream().map(BinSkuForm::getClientSkuId)
                .collect(Collectors.toList());

        HashMap<String, Long> clientToGlobalSkuIdMap = new HashMap<>();
        for (String clientSkuId : clientSkuIdList) {
            ProductPojo productPojo = productService.selectByClientSkuId(clientSkuId);
            if (!isNull(productPojo)) {
                clientToGlobalSkuIdMap.put(productPojo.getClientSkuId(), productPojo.getGlobalSkuId());
            }
        }
        return clientToGlobalSkuIdMap;
    }

    private void checkClientSkuIdExist(HashMap<String, Long> clientToGlobalSkuIdMap, List<BinSkuForm> binSkuFormList)
            throws ApiException {
        Integer row = 0;
        List<ErrorData> errorFormList = new ArrayList<>();
        for (BinSkuForm binSkuForm : binSkuFormList) {
            if (!clientToGlobalSkuIdMap.containsKey(binSkuForm.getClientSkuId())) {
                errorFormList.add(new ErrorData(row, "clientSkuId does not exist, clientSkuId : "
                        + binSkuForm.getClientSkuId()));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }

    private void checkBinIdExists(List<BinSkuForm> binSkuFormList) throws ApiException {
        Integer row = 1;
        List<ErrorData> errorFormList = new ArrayList<>();
        for (BinSkuForm binSkuForm : binSkuFormList) {
            if (isNull(binService.selectById(binSkuForm.getBinId()))) {
                errorFormList.add(new ErrorData(row, "bin id doest not exist, binId : " + binSkuForm.getBinId()));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }
}
