package assure.util;

import assure.model.*;
import assure.pojo.*;
import assure.spring.ApiException;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class Helper {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

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

    public static void checkDuplicateProductsProductForm(List<ProductForm> productFormList) throws ApiException {

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

    public static <T> void validateList(String name, List<T> formList) throws ApiException {
        if (CollectionUtils.isEmpty(formList)) {
            throw new ApiException(name + " List cannot be empty");
        }

        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (T form : formList) {
            Set<ConstraintViolation<T>> constraintViolations =
                    validator.validate(form);

            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                errorFormList.add(new ErrorData(row, constraintViolation.getPropertyPath().toString() + " " + constraintViolation.getMessage()));
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

    public static ProductPojo convertProductUpdateFormToPojo(ProductUpdateForm productUpdateForm, Long globalSkuId, Long clientId) {
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

    public static <T> void validate(T form) throws ApiException {
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(form);
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            errorFormList.add(new ErrorData(row, constraintViolation.getPropertyPath().toString()
                    + " " + constraintViolation.getMessage()));
        }
        row++;
        throwErrorIfNotEmpty(errorFormList);
    }

    public static BinData convertBinPojoToData(BinPojo binPojo) {
        BinData binData = new BinData();
        binData.setId(binPojo.getBinId());

        return binData;
    }

    public static List<BinData> convertListBinPojoToData(List<BinPojo> binPojoList) {
        List<BinData> binDataList = new ArrayList<>();
        for (BinPojo binPojo : binPojoList) {
            binDataList.add(convertBinPojoToData(binPojo));
        }
        return binDataList;
    }

    public static BinSkuPojo convertBinSkuFormToPojo(BinSkuForm binSkuForm, Long globalSkuId) {

        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setBinId(binSkuForm.getBinId());
        binSkuPojo.setGlobalSkuId(globalSkuId);
        binSkuPojo.setQuantity(binSkuForm.getQuantity());

        return binSkuPojo;

    }

    public static List<BinSkuPojo> convertListBinSkuFormToPojo(List<BinSkuForm> binSkuFormList, HashMap<String, Long> clientToGlobalSkuIdMap) {
        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        for (BinSkuForm binSkuForm : binSkuFormList) {
            binSkuPojoList.add(convertBinSkuFormToPojo(binSkuForm, clientToGlobalSkuIdMap.get(binSkuForm.getClientSkuId())));
        }

        return binSkuPojoList;
    }

    public static BinSkuData convertBinSkuPojoToData(BinSkuPojo binSkuPojo) {
        BinSkuData binSkuData = new BinSkuData();
        binSkuData.setBinId(binSkuPojo.getBinId());
        binSkuData.setGlobalSkuId(binSkuPojo.getGlobalSkuId());
        binSkuData.setQuantity(binSkuPojo.getQuantity());
        binSkuData.setId(binSkuPojo.getId());
        return binSkuData;
    }

    public static List<BinSkuData> convertListBinSkuPojoToData(List<BinSkuPojo> binSkuPojoList) {
        List<BinSkuData> binSkuDataList = new ArrayList<>();
        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            binSkuDataList.add(convertBinSkuPojoToData(binSkuPojo));
        }
        return binSkuDataList;
    }

    public static BinSkuPojo convertBinSkuUpdateFormToPojo(BinSkuUpdateForm binSkuUpdateForm, Long id) {
        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setQuantity(binSkuUpdateForm.getQuantity());
        binSkuPojo.setId(id);

        return binSkuPojo;
    }

    public static ChannelData convertChannelPojoToData(ChannelPojo channelPojo) {
        ChannelData channelData = new ChannelData();
        channelData.setName(channelPojo.getName());
        channelData.setInvoiceTypes(channelPojo.getInvoiceType());

        return channelData;
    }

    public static List<ChannelData> convertChannelPojoListToData(List<ChannelPojo> channelPojoList) {
        List<ChannelData> channelDataList = new ArrayList<>();
        for (ChannelPojo channelPojo : channelPojoList) {
            channelDataList.add(convertChannelPojoToData(channelPojo));
        }

        return channelDataList;
    }
    public static void normalizeChannelPojo(ChannelPojo channelPojo){
        channelPojo.setName(channelPojo.getName().toUpperCase());
    }
    public static ChannelPojo convertChannelFormToPojo(ChannelForm channelForm) {
        ChannelPojo channelPojo = new ChannelPojo();
        channelPojo.setName(channelForm.getName());
        channelPojo.setInvoiceType(channelForm.getInvoiceType());

        return channelPojo;
    }
    public static <T> void validateAddPojo(T pojo, List<String>excludeList) throws ApiException {
        if(isNull(pojo)){
            throw new ApiException(" Pojo object can not be null");
        }
        try {

            Field[] fields = pojo.getClass().getDeclaredFields();
            for (Field m : fields) {
                m.setAccessible(true);
                System.out.println(m.get(pojo));
                if(isNull(m.get(pojo)) && !excludeList.contains(m.getName())){
                    throw new ApiException(m.getName() + " cannot be null in Pojo object");
                }
            }
        } catch (IllegalAccessException err) {
            System.out.println(err);
        }
    }
    public static <T> void validateAddPojoList(List<T> pojoList, List<String>excludeList, Long maxListSize) throws ApiException {
        if(CollectionUtils.isEmpty(pojoList)){
            throw new ApiException("List cannot be empty or null");
        }

        if(pojoList.size()>maxListSize){
            throw new ApiException("list size more than max limit, limit : " + maxListSize);
        }

        for (T pojo : pojoList) {
            validateAddPojo(pojo,excludeList);
        }
    }
    public static void checkDuplicateChannelListingFormList(List<ChannelListingForm> channelListingFormList) throws ApiException {
        HashSet<String> setChannelSkuId = new HashSet<>();
        HashSet<String> setClientSkuId = new HashSet<>();
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (ChannelListingForm channelListingForm : channelListingFormList) {
            if (setChannelSkuId.contains(channelListingForm.getChannelSkuId())) {
                errorFormList.add(new ErrorData(row, "duplicate values of channelSkuId"));
            }
            if (setClientSkuId.contains(channelListingForm.getClientSkuId())) {
                errorFormList.add(new ErrorData(row, "duplicate values of clientSkuId"));
            }
            setChannelSkuId.add(channelListingForm.getChannelSkuId());
            setClientSkuId.add(channelListingForm.getClientSkuId());
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }

    public static void checkDuplicatesOrderItemFormList(List<OrderItemForm> orderItemFormList) throws ApiException {
        Set<String>clientSkuIdSet = orderItemFormList.stream().map(OrderItemForm::getClientSkuId).collect(Collectors.toSet());
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;

        for (OrderItemForm orderItemForm : orderItemFormList) {
            if(clientSkuIdSet.contains(orderItemForm.getClientSkuId())){
                errorFormList.add(new ErrorData(row, "Duplicate client sku id"));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }
    public static OrderPojo createOrderPojo(Long clientId, Long customerId,Long channelId, String channelOrderId){
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setChannelOrderId(channelOrderId);
        orderPojo.setClientId(clientId);
        orderPojo.setChannelId(channelId);
        orderPojo.setCustomerId(customerId);
        orderPojo.setStatus(OrderStatus.CREATED);

        return orderPojo;
    }
    public static List<OrderItemPojo> transformAndConvertOrderItemFormToPojo(Long orderId,
                                                                             List<OrderItemForm> orderItemFormList,
                                                                             Map<String, Long> clientSkuIdToGlobalSkuIdMap){
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItemFormList) {
            OrderItemPojo orderItemPojo = new OrderItemPojo();
            orderItemPojo.setOrderId(orderId);
            orderItemPojo.setOrderedQuantity(orderItemForm.getQuantity());
            orderItemPojo.setGlobalSkuId(clientSkuIdToGlobalSkuIdMap.get(orderItemForm.getClientSkuId()));
            orderItemPojo.setSellingPricePerUnit(orderItemForm.getSellingPricePerUnit());
            orderItemPojoList.add(orderItemPojo);
        }
        return orderItemPojoList;
     }
}
