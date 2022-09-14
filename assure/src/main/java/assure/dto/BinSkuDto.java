package assure.dto;

import assure.model.BinSkuData;
import assure.model.BinSkuForm;
import assure.model.BinSkuItemForm;
import assure.model.BinSkuUpdateForm;
import assure.pojo.BinSkuPojo;
import assure.pojo.ProductPojo;
import assure.service.*;
import assure.spring.ApiException;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static assure.util.ConversionUtil.*;
import static assure.util.DataUtil.checkClientSkuIdExist;
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

    @Transactional(rollbackFor = ApiException.class)
    public Integer add(BinSkuForm binSkuForm) throws ApiException {
        List<BinSkuItemForm> binSkuFormList = binSkuForm.getBinSkuItemFormList();
        validateList("BinSku", binSkuFormList, MAX_LIMIT);

        Long clientId = binSkuForm.getClientId();
        partyService.getCheck(clientId);

        List<String> clientSkuIdList = binSkuFormList.stream().map(BinSkuItemForm::getClientSkuId)
                .collect(Collectors.toList());
        Map<String, Long> clientToGlobalSkuIdMap = productService.getCheckClientSkuId(clientSkuIdList, clientId);

        List<BinSkuPojo> binSkuPojoList = binSkuService.add(convertListBinSkuFormToPojo(binSkuFormList, clientToGlobalSkuIdMap));
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
        //TODO change to negative and positive increase and decrease and not pair get pojo first
        //doing same id call in one transaction is same wont cause extra
        //TODO make 2 inventory update increase and decrease;
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
                map(BinSkuPojo::getGlobalSkuId).collect(Collectors.toSet()));
        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            binSkuDataList.add(convertBinSkuPojoToData(binSkuPojo,
                    globalSkuIdToPojo.get(binSkuPojo.getGlobalSkuId()).getClientSkuId()));
        }
        return binSkuDataList;
    }
}
