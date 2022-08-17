package assure.dto;

import assure.model.BinSkuData;
import assure.model.BinSkuForm;
import assure.model.BinSkuUpdateForm;
import assure.model.ErrorData;
import assure.pojo.ProductPojo;
import assure.service.BinService;
import assure.service.BinSkuService;
import assure.service.ProductService;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static assure.util.Helper.*;
import static java.util.Objects.isNull;

@Service
public class BinSkuDto {

    @Autowired
    private BinSkuService binSkuService;
    @Autowired
    private ProductService productService;

    @Autowired
    private BinService binService;

    public Integer add(List<BinSkuForm> binSkuFormList) throws ApiException {
        Long maxListSize = 1000L;//TODO private top class
        if (binSkuFormList.size() > maxListSize) {
            throw new ApiException("List size more than limit, limit : " + maxListSize);
        }
        validateBinSkuFormList(binSkuFormList);
        checkDuplicateProductsBinSkuForm(binSkuFormList);
        HashMap<String, Long> clientToGlobalSkuIdMap = getClientToGlobalSkuIdMap(binSkuFormList);
        checkBinIdExists(binSkuFormList);
        checkClientSkuIdExist(clientToGlobalSkuIdMap, binSkuFormList);

        binSkuService.add(convertListBinSkuFormToPojo(binSkuFormList, clientToGlobalSkuIdMap));

        return binSkuFormList.size();
    }

    public List<BinSkuData> select(Integer pageNumber) {
        Integer pageSize = 10; //TODO private static final
        return convertListBinSkuPojoToData(binSkuService.select(pageNumber, pageSize));
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
            if (clientToGlobalSkuIdMap.containsKey(binSkuForm.getClientSkuId())) {
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
