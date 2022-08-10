package assure.util;

import assure.model.*;
import assure.pojo.BinPojo;
import assure.pojo.ClientPojo;
import assure.pojo.ProductPojo;
import assure.spring.ApiException;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static assure.util.DataUtil.validateNullCheck;
import static java.util.Objects.isNull;

public class Helper {

    public static ClientPojo convertClientFormToPojo(ClientForm clientForm) {
        ClientPojo clientPojo = new ClientPojo();
        clientPojo.setName(clientForm.getName());
        clientPojo.setType(clientForm.getTypes());

        return clientPojo;
    }


    public static ClientData convertClientPojoToData(ClientPojo clientPojo) {
        ClientData clientData = new ClientData();
        clientData.setName(clientPojo.getName());
        clientData.setType(clientPojo.getType());
        clientData.setId(clientPojo.getId());
        return clientData;
    }

    public static List<ClientData> convertListClientPojoToData(List<ClientPojo> clientPojoList) {
        List<ClientData> clientDataList = new ArrayList<>();
        for (ClientPojo clientPojo : clientPojoList) {
            clientDataList.add(convertClientPojoToData(clientPojo));
        }

        return clientDataList;
    }

    public static List<ClientPojo> convertListClientFormToPojo(List<ClientForm> clientFormList) {
        List<ClientPojo> clientPojoList = new ArrayList<>();
        for (ClientForm clientForm : clientFormList) {
            clientPojoList.add(convertClientFormToPojo(clientForm));
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

    public static List<ProductPojo> convertListProductFormToPojo(List<ProductForm> productFormList, Long clientId) {
        List<ProductPojo> productPojoList = new ArrayList<>();
        for (ProductForm productForm : productFormList) {
            productPojoList.add(convertProductFormToPojo(productForm, clientId));
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

    public static void checkDuplicateProducts(List<ProductForm> productFormList) throws ApiException {

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

    public static void validateList(List<ProductForm> productFormList) throws ApiException {
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

}
