package assure.dto;

import assure.model.BinSkuForm;
import assure.model.ErrorForm;
import assure.pojo.BinPojo;
import assure.pojo.ProductPojo;
import assure.services.BinServices;
import assure.services.BinSkuServices;
import assure.services.ProductServices;
import assure.spring.ApiException;
import com.google.common.collect.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

    private HashMap<String, Long> getClientToGlobalSkuIdMap(List<BinSkuForm> binSkuFormList) {

        Integer batchSize = 5;

        List<String> clientSkuIdList = binSkuFormList.stream().map(BinSkuForm::getClientSkuId)
                .collect(Collectors.toList());

        Iterator<List<String>> itr = Iterables.partition(clientSkuIdList, (clientSkuIdList.size()) / batchSize).iterator();
        HashMap<String, Long> clientToGlobalSkuIdMap = new HashMap<>();

        while (itr.hasNext()) {
            List<String> clientSkuIdBatch = new ArrayList<>(itr.next());
            List<ProductPojo> productPojoList = productServices.selectByClientSkuIdList(clientSkuIdBatch);
            for (ProductPojo productPojo : productPojoList) {
                clientToGlobalSkuIdMap.put(productPojo.getClientSkuId(), productPojo.getGlobalSkuId());
            }

        }
        return clientToGlobalSkuIdMap;
    }

    private void checkClientSkuIdExist(HashMap<String, Long> clientToGlobalSkuIdMap, List<BinSkuForm> binSkuFormList) throws ApiException {
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

        Set<Long> binIdSet = binSkuFormList.stream().map(BinSkuForm::getBinId).collect(Collectors.toSet());
        Iterator<List<Long>> itr = Iterables.partition(binIdSet, (binIdSet.size()) / batchSize).iterator();
        while (itr.hasNext()) {
            List<Long> binIdBatch = new ArrayList<>(itr.next());
            List<BinPojo> binPojoList = binServices.selectByIdList(binIdBatch);
            List<Long> binIdList = binPojoList.stream().map(BinPojo::getBinId).collect(Collectors.toList());
            binIdSet.removeAll(binIdList);
        }
        Integer row = 1;
        List<ErrorForm> errorFormList = new ArrayList<>();
        for (BinSkuForm binSkuForm : binSkuFormList) {
            if (!binIdSet.contains(binSkuForm.getBinId())) {
                errorFormList.add(new ErrorForm(row, "bin id doest not exist, binId : " + binSkuForm.getBinId()));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }
}
