package assure.dto;

import assure.model.BinSkuData;
import assure.model.BinSkuForm;
import assure.model.ErrorForm;
import assure.pojo.BinPojo;
import assure.pojo.ProductPojo;
import assure.services.BinServices;
import assure.services.BinSkuServices;
import assure.services.ProductServices;
import assure.spring.ApiException;
import assure.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static assure.util.Helper.*;

@Service
public class BinSkuDto {

    @Autowired
    private BinSkuServices binSkuServices;
    @Autowired
    private ProductServices productServices;

    @Autowired
    private BinServices binServices;

    public Integer add(List<BinSkuForm> binSkuFormList) throws ApiException {
        Long maxListSize = 1000L;
        if (binSkuFormList.size() > maxListSize) {
            throw new ApiException("List size more than limit, limit : " + maxListSize);
        }
        validateBinSkuFormList(binSkuFormList);
        checkDuplicateProductsBinSkuForm(binSkuFormList);
        HashMap<String, Long> clientToGlobalSkuIdMap = getClientToGlobalSkuIdMap(binSkuFormList);
        checkBinIdExists(binSkuFormList);
        checkClientSkuIdExist(clientToGlobalSkuIdMap, binSkuFormList);

        binSkuServices.add(convertListBinSkuFormToPojo(binSkuFormList, clientToGlobalSkuIdMap));

        return binSkuFormList.size();
    }

    public List<BinSkuData> select(Integer pageNumber) {
        Integer pageSize = 10;
        return convertListBinSkuPojoToData(binSkuServices.select(pageNumber, pageSize));
    }

    private HashMap<String, Long> getClientToGlobalSkuIdMap(List<BinSkuForm> binSkuFormList) {

        Integer batchSize = 5;

        List<String> clientSkuIdList = binSkuFormList.stream().map(BinSkuForm::getClientSkuId)
                .collect(Collectors.toList());

        List<List<String>> subLists = DataUtil.partition(clientSkuIdList, (int) Math.ceil(((double) clientSkuIdList.size()) / batchSize));
        HashMap<String, Long> clientToGlobalSkuIdMap = new HashMap<>();
        for (List<String> subList : subLists) {
            List<ProductPojo> productPojoList = productServices.selectByClientSkuIdList(subList);
            for (ProductPojo productPojo : productPojoList) {
                clientToGlobalSkuIdMap.put(productPojo.getClientSkuId(), productPojo.getGlobalSkuId());
            }

        }

        return clientToGlobalSkuIdMap;
    }

    private void checkClientSkuIdExist(HashMap<String, Long> clientToGlobalSkuIdMap, List<BinSkuForm> binSkuFormList)
            throws ApiException {
        Integer row = 0;
        List<ErrorForm> errorFormList = new ArrayList<>();
        for (BinSkuForm binSkuForm : binSkuFormList) {
            if (!clientToGlobalSkuIdMap.containsKey(binSkuForm.getClientSkuId())) {
                errorFormList.add(new ErrorForm(row, "clientSkuId does not exist, clientSkuId : "
                        + binSkuForm.getClientSkuId()));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }

    private void checkBinIdExists(List<BinSkuForm> binSkuFormList) throws ApiException {
        Integer batchSize = 5;

        List<Long> binIdFormList = binSkuFormList.stream().map(BinSkuForm::getBinId).collect(Collectors.toList());
        List<List<Long>> subLists = DataUtil.partition(binIdFormList, (int) Math.ceil(((double) binIdFormList.size()) / batchSize));
        for (List<Long> subList : subLists) {
            List<BinPojo> binPojoList = binServices.selectByIdList(subList);
            List<Long> binIdList = binPojoList.stream().map(BinPojo::getBinId).collect(Collectors.toList());
            binIdFormList.removeAll(binIdList);
        }

        Integer row = 1;
        List<ErrorForm> errorFormList = new ArrayList<>();
        for (BinSkuForm binSkuForm : binSkuFormList) {
            if (binIdFormList.contains(binSkuForm.getBinId())) {
                errorFormList.add(new ErrorForm(row, "bin id doest not exist, binId : " + binSkuForm.getBinId()));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }
}
