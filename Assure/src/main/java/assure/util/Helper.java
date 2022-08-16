package assure.util;

import assure.model.*;
import assure.pojo.BinPojo;
import assure.pojo.BinSkuPojo;
import assure.pojo.ClientPojo;
import assure.pojo.ProductPojo;
import assure.spring.ApiException;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static assure.util.DataUtil.validateNullCheck;
import static java.util.Objects.isNull;

public class Helper {

    public static ClientPojo convertClientFormToPojo(ConsumerForm consumerForm) {
        ClientPojo clientPojo = new ClientPojo();
        clientPojo.setName(consumerForm.getName());
        clientPojo.setType(consumerForm.getTypes());

        return clientPojo;
    }


    public static ConsumerData convertClientPojoToData(ClientPojo clientPojo) {
        ConsumerData consumerData = new ConsumerData();
        consumerData.setName(clientPojo.getName());
        consumerData.setType(clientPojo.getType());
        consumerData.setId(clientPojo.getId());
        return consumerData;
    }

    public static List<ConsumerData> convertListClientPojoToData(List<ClientPojo> clientPojoList) {
        List<ConsumerData> consumerDataList = new ArrayList<>();
        for (ClientPojo clientPojo : clientPojoList) {
            consumerDataList.add(convertClientPojoToData(clientPojo));
        }

        return consumerDataList;
    }

    public static List<ClientPojo> convertListClientFormToPojo(List<ConsumerForm> consumerFormList) {
        List<ClientPojo> clientPojoList = new ArrayList<>();
        for (ConsumerForm consumerForm : consumerFormList) {
            clientPojoList.add(convertClientFormToPojo(consumerForm));
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

    public static String transformErrorList(List<ErrorForm> errorFormList) {
        String err = "";
        for (ErrorForm errorForm : errorFormList) {
            err += errorForm.toString();
        }
        System.out.println(err);
        return err;
    }

    public static <T> void checkDuplicateProductsProductForm(List<ProductForm> productFormList) throws ApiException {

        HashSet<String> set = new HashSet<>();
        List<ErrorForm> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (ProductForm productForm : productFormList) {
            if (set.contains(productForm.getClientSkuId())) {
                errorFormList.add(new ErrorForm(row, "duplicate values of clientSkuId"));
            }
            set.add(productForm.getClientSkuId());
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }
    public static void checkDuplicateProductsBinSkuForm(List<BinSkuForm> binSkuFormList) throws ApiException {

        HashSet<String> set = new HashSet<>();
        List<ErrorForm> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (BinSkuForm binSkuForm : binSkuFormList) {
            if (set.contains(binSkuForm.getClientSkuId())) {
                errorFormList.add(new ErrorForm(row, "duplicate values of clientSkuId"));
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

        List<ErrorForm> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (ProductForm productForm : productFormList) {
            if (!validateNullCheck(productForm)) {
                errorFormList.add(new ErrorForm(row, "value cannot be null or empty"));
            }
            if (!DataUtil.validateMRP(productForm.getMrp()) && !isNull(productForm.getMrp())) {
                errorFormList.add(new ErrorForm(row, "MRP should be a positive number"));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }
    public static void validateBinSkuFormList(List<BinSkuForm> binSkuFormList) throws ApiException {

        if(CollectionUtils.isEmpty(binSkuFormList)){
            throw new ApiException("Empty body");
        }
        List<ErrorForm> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (BinSkuForm binSkuForm : binSkuFormList) {
            if (!validateNullCheck(binSkuForm)) {
                errorFormList.add(new ErrorForm(row, "value cannot be null or empty"));
            }
            if (binSkuForm.getQuantity()<0) {
                errorFormList.add(new ErrorForm(row, "quantity should be a positive number"));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }

    public static void throwErrorIfNotEmpty(List<ErrorForm> errorFormList) throws ApiException {
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
        if(!validateNullCheck(productUpdateForm)){
            throw new ApiException("value cannot be null or empty");
        }
        if (!DataUtil.validateMRP(productUpdateForm.getMrp()) && !isNull(productUpdateForm.getMrp())) {
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
}
