package assure.dto;

import assure.model.BinSkuForm;
import assure.model.ErrorForm;
import assure.pojo.ProductPojo;
import assure.services.BinSkuServices;
import assure.services.ProductServices;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static assure.util.Helper.checkDuplicateProducts;
import static assure.util.Helper.*;

@Service
public class BinSkuDto {

    @Autowired
    private BinSkuServices binSkuServices;
    @Autowired
    private ProductServices productServices;

    public Integer add(List<BinSkuForm> binSkuFormList) throws ApiException {
        Long maxListSize = 1000L;
        if(binSkuFormList.size()>maxListSize){
            throw new ApiException("List size more than limit, limit : " + maxListSize);
        }
        validateList(binSkuFormList);
        checkDuplicateProducts(binSkuFormList);
        HashMap<String,Long> clientToGlobalSkuIdMap = getClientToGlobalSkuIdMap(binSkuFormList);
        checkClientSkuIdExist(clientToGlobalSkuIdMap,binSkuFormList);

        binSkuServices.add(convertListBinSkuFormToPojo(binSkuFormList,clientToGlobalSkuIdMap));

        return binSkuFormList.size();
    }

    private HashMap<String,Long> getClientToGlobalSkuIdMap(List<BinSkuForm> binSkuFormList){
        Set<String> clientSkuIdSet = binSkuFormList.stream().map(BinSkuForm::getClientSkuId)
                .collect(Collectors.toSet());
        HashMap<String,Long> clientToGlobalSkuIdMap = new HashMap<>();

        Integer pageNumber = 0;
        Integer pageSize = 5;

        while(true){
            List<ProductPojo> productPojoList = productServices.select(pageNumber, pageSize);
            if(CollectionUtils.isEmpty(productPojoList)){
                break;
            }
            HashMap<String,Long> map = new HashMap<>();
            for(ProductPojo productPojo : productPojoList){
                map.put(productPojo.getClientSkuId(),productPojo.getGlobalSkuId());
            }

            for(String clientSkuId : clientSkuIdSet){
                if(map.containsKey(clientSkuId)){
                    clientToGlobalSkuIdMap.put(clientSkuId,map.get(clientSkuId));
                    clientSkuIdSet.remove(clientSkuId);
                }
            }
            if(CollectionUtils.isEmpty(clientSkuIdSet)){
                break;
            }
            pageNumber++;
        }
        return clientToGlobalSkuIdMap;
    }
    private void checkClientSkuIdExist(HashMap<String,Long> clientToGlobalSkuIdMap,List<BinSkuForm> binSkuFormList) throws ApiException {
        Integer row = 0;
        List<ErrorForm> errorFormList = new ArrayList<>();
        for(BinSkuForm binSkuForm : binSkuFormList){
            if(!clientToGlobalSkuIdMap.containsKey(binSkuForm.getClientSkuId())){
                errorFormList.add(new ErrorForm(row, "clientSkuId does not exist, clientSkuId : "
                        + binSkuForm.getClientSkuId()));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }
}
