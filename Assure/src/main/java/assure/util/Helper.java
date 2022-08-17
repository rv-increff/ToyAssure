package assure.util;

import assure.model.*;
import assure.pojo.*;
import assure.spring.ApiException;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static assure.util.ValidationUtils.checkNotNull;
import static java.util.Objects.isNull;

public class Helper {

    public static PartyPojo convertClientFormToPojo(PartyForm partyForm) {
        PartyPojo clientPojo = new PartyPojo();
        clientPojo.setName(partyForm.getName());
        clientPojo.setType(partyForm.getTypes());

        return clientPojo;
    }


    public static PartyData convertPartyPojoToData(PartyPojo clientPojo) {
        PartyData partyData = new PartyData();
        partyData.setName(clientPojo.getName());
        partyData.setType(clientPojo.getType());
        partyData.setId(clientPojo.getId());
        return partyData;
    }

    public static List<PartyData> convertListPartyPojoToData(List<PartyPojo> clientPojoList) {
        List<PartyData> partyDataList = new ArrayList<>();
        for (PartyPojo clientPojo : clientPojoList) {
            partyDataList.add(convertPartyPojoToData(clientPojo));
        }

        return partyDataList;
    }

    public static List<PartyPojo> convertListPartyFormToPojo(List<PartyForm> partyFormList) {
        List<PartyPojo> clientPojoList = new ArrayList<>();
        for (PartyForm partyForm : partyFormList) {
            clientPojoList.add(convertClientFormToPojo(partyForm));
        }

        return clientPojoList;
    }

    public static ProductPojo convertProductFormToPojo(ProductForm productForm, Long clientId) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setClientId(clientId);
        productPojo.setBrandId(productForm.getBrandId());
        productPojo.setDescription(productForm.getDescription());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setName(productForm.getName());
        productPojo.setClientSkuId(productForm.getClientSkuId());
        return productPojo;
    }

    public static List<ProductPojo> convertListProductFormToPojo(List<ProductForm> productFormList, Long consumerId) {
        List<ProductPojo> productPojoList = new ArrayList<>();
        for (ProductForm productForm : productFormList) {
            productPojoList.add(convertProductFormToPojo(productForm, consumerId));
        }
        return productPojoList;
    }

    public static ProductData convertProductPojoToData(ProductPojo productPojo) {
        ProductData productData = new ProductData();
        productData.setBrandId(productPojo.getBrandId());
        productData.setDescription(productPojo.getDescription());
        productData.setGlobalSkuId(productPojo.getGlobalSkuId());
        productData.setMrp(productPojo.getMrp());
        productData.setName(productPojo.getName());
        productData.setClientId(productPojo.getClientId());
        productData.setClientSkuId(productPojo.getClientSkuId());

        return productData;
    }

    public static List<ProductData> convertListProductPojoToData(List<ProductPojo> productPojoList) {
        List<ProductData> productDataList = new ArrayList<>();
        for (ProductPojo productPojo : productPojoList) {
            productDataList.add(convertProductPojoToData(productPojo));
        }
        return productDataList;
    }

    public static String transformErrorList(List<ErrorData> errorFormList) {
        String err = "";
        for (ErrorData errorForm : errorFormList) {
            err += errorForm.toString();
        }
        System.out.println(err);
        return err;
    }

    public static <T> void checkDuplicateProductsProductForm(List<ProductForm> productFormList) throws ApiException {

        HashSet<String> set = new HashSet<>();
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (ProductForm productForm : productFormList) {
            if (set.contains(productForm.getClientSkuId())) {
                errorFormList.add(new ErrorData(row, "duplicate values of clientSkuId"));
            }
            set.add(productForm.getClientSkuId());
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }
    public static void checkDuplicateProductsBinSkuForm(List<BinSkuForm> binSkuFormList) throws ApiException {

        HashSet<String> set = new HashSet<>();
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (BinSkuForm binSkuForm : binSkuFormList) {
            if (set.contains(binSkuForm.getClientSkuId())) {
                errorFormList.add(new ErrorData(row, "duplicate values of clientSkuId"));
            }
            set.add(binSkuForm.getClientSkuId());
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }

    public static void validateList(List<ProductForm> productFormList) throws ApiException {
        if(CollectionUtils.isEmpty(productFormList)){
            throw new ApiException("Empty body");
        }

        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (ProductForm productForm : productFormList) {
            if (!checkNotNull(productForm)) {
                errorFormList.add(new ErrorData(row, "value cannot be null or empty"));
            }
            if (!ValidationUtils.validateMRP(productForm.getMrp()) && !isNull(productForm.getMrp())) {
                errorFormList.add(new ErrorData(row, "MRP should be a positive number"));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }
    public static void validateBinSkuFormList(List<BinSkuForm> binSkuFormList) throws ApiException {

        if(CollectionUtils.isEmpty(binSkuFormList)){
            throw new ApiException("BinSku List cannot be empty");
        }
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (BinSkuForm binSkuForm : binSkuFormList) {
            if (!checkNotNull(binSkuForm)) {
                errorFormList.add(new ErrorData(row, "value cannot be null or empty"));
            }
            if (binSkuForm.getQuantity()<0) {
                errorFormList.add(new ErrorData(row, "quantity should be a positive number"));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }

    public static void throwErrorIfNotEmpty(List<ErrorData> errorFormList) throws ApiException {
        if (!CollectionUtils.isEmpty(errorFormList)) {
            throw new ApiException(errorFormList);
        }
    }

    public static ProductPojo convertProductUpdateFormToPojo(ProductUpdateForm productUpdateForm, Long globalSkuId,Long clientId) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setGlobalSkuId(globalSkuId);
        productPojo.setClientSkuId(productUpdateForm.getClientSkuId());
        productPojo.setMrp(productUpdateForm.getMrp());
        productPojo.setName(productUpdateForm.getName());
        productPojo.setDescription(productUpdateForm.getDescription());
        productPojo.setClientId(clientId);
        productPojo.setBrandId(productUpdateForm.getBrandId());

        return productPojo;
    }
    public static void validate(ProductUpdateForm productUpdateForm) throws ApiException {
        if(!checkNotNull(productUpdateForm)){
            throw new ApiException("value cannot be null or empty");
        }
        if (!ValidationUtils.validateMRP(productUpdateForm.getMrp()) && !isNull(productUpdateForm.getMrp())) {
            throw new ApiException("MRP should be a positive number");
        }
    }

    public static BinData convertBinPojoToData(BinPojo binPojo){
        BinData binData = new BinData();
        binData.setId(binPojo.getBinId());

        return binData;
    }

    public static List<BinData> convertListBinPojoToData(List<BinPojo> binPojoList){
        List<BinData> binDataList = new ArrayList<>();
        for(BinPojo binPojo : binPojoList){
            binDataList.add(convertBinPojoToData(binPojo));
        }
        return binDataList;
    }

    public static BinSkuPojo convertBinSkuFormToPojo(BinSkuForm binSkuForm, Long globalSkuId){

        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setBinId(binSkuForm.getBinId());
        binSkuPojo.setGlobalSkuId(globalSkuId);
        binSkuPojo.setQuantity(binSkuForm.getQuantity());

        return binSkuPojo;

    }

    public static List<BinSkuPojo> convertListBinSkuFormToPojo(List<BinSkuForm> binSkuFormList, HashMap<String,Long> clientToGlobalSkuIdMap){
        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        for(BinSkuForm binSkuForm : binSkuFormList){
            binSkuPojoList.add(convertBinSkuFormToPojo(binSkuForm,clientToGlobalSkuIdMap.get(binSkuForm.getClientSkuId())));
        }

        return binSkuPojoList;
    }

    public static BinSkuData convertBinSkuPojoToData(BinSkuPojo binSkuPojo){
        BinSkuData binSkuData = new BinSkuData();
        binSkuData.setBinId(binSkuPojo.getBinId());
        binSkuData.setGlobalSkuId(binSkuPojo.getGlobalSkuId());
        binSkuData.setQuantity(binSkuPojo.getQuantity());
        binSkuData.setId(binSkuPojo.getId());
        return binSkuData;
    }

    public static List<BinSkuData> convertListBinSkuPojoToData(List<BinSkuPojo> binSkuPojoList){
        List<BinSkuData> binSkuDataList = new ArrayList<>();
        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            binSkuDataList.add(convertBinSkuPojoToData(binSkuPojo));
        }
        return binSkuDataList;
    }

    public static BinSkuPojo convertBinSkuUpdateFormToPojo(BinSkuUpdateForm binSkuUpdateForm, Long id){
        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setQuantity(binSkuUpdateForm.getQuantity());
        binSkuPojo.setId(id);

        return binSkuPojo;
    }
    public static ChannelData convertChannelPojoToData(ChannelPojo channelPojo){
        ChannelData channelData = new ChannelData();
        channelData.setName(channelPojo.getName());
        channelData.setInvoiceTypes(channelPojo.getInvoiceType());

        return channelData;
    }
    public static List<ChannelData> convertChannelPojoListToData(List<ChannelPojo> channelPojoList){
        List<ChannelData> channelDataList = new ArrayList<>();
        for (ChannelPojo channelPojo : channelPojoList) {
            channelDataList.add(convertChannelPojoToData(channelPojo));
        }

        return channelDataList;
    }
}
