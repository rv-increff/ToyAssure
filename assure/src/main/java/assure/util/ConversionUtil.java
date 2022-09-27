package assure.util;

import assure.model.*;
import assure.pojo.*;
import assure.spring.ApiException;
import commons.model.*;
import javafx.util.Pair;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static assure.util.ValidationUtil.throwErrorIfNotEmpty;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

public class ConversionUtil {
    public static PartyPojo convertClientFormToPojo(PartyForm partyForm) {
        if (isNull(partyForm))
            return new PartyPojo();

        PartyPojo clientPojo = new PartyPojo();
        clientPojo.setName(partyForm.getName());
        clientPojo.setType(partyForm.getType());

        return clientPojo;
    }

    public static PartyData convertPartyPojoToData(PartyPojo partyPojo) {
        if (isNull(partyPojo))
            return new PartyData();

        PartyData partyData = new PartyData();
        partyData.setName(partyPojo.getName());
        partyData.setType(partyPojo.getType());
        partyData.setId(partyPojo.getId());

        return partyData;
    }

    public static List<PartyData> convertListPartyPojoToData(List<PartyPojo> partyPojoList) {
        if (CollectionUtils.isEmpty(partyPojoList))
            return new ArrayList<>();

        List<PartyData> partyDataList = new ArrayList<>();
        for (PartyPojo clientPojo : partyPojoList)
            partyDataList.add(convertPartyPojoToData(clientPojo));

        return partyDataList;
    }

    public static List<PartyPojo> convertListPartyFormToPojo(List<PartyForm> partyFormList) {
        if (CollectionUtils.isEmpty(partyFormList))
            return new ArrayList<>();

        List<PartyPojo> clientPojoList = new ArrayList<>();
        for (PartyForm partyForm : partyFormList)
            clientPojoList.add(convertClientFormToPojo(partyForm));

        return clientPojoList;
    }

    public static List<ProductPojo> convertListProductFormToPojo(List<ProductForm> productFormList, Long consumerId) {
        if (CollectionUtils.isEmpty(productFormList))
            return new ArrayList<>();

        List<ProductPojo> productPojoList = new ArrayList<>();
        for (ProductForm productForm : productFormList)
            productPojoList.add(convertProductFormToPojo(productForm, consumerId));

        return productPojoList;
    }

    public static ProductData convertProductPojoToData(ProductPojo productPojo) {
        if (isNull(productPojo))
            return new ProductData();

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
        if (CollectionUtils.isEmpty(productPojoList))
            return new ArrayList<>();

        List<ProductData> productDataList = new ArrayList<>();
        for (ProductPojo productPojo : productPojoList) {
            productDataList.add(convertProductPojoToData(productPojo));
        }
        return productDataList;
    }

    public static ProductPojo convertProductFormToPojo(ProductForm productForm, Long clientId) {
        if (isNull(productForm))
            return new ProductPojo();

        ProductPojo productPojo = new ProductPojo();
        productPojo.setClientId(clientId);
        productPojo.setBrandId(productForm.getBrandId());
        productPojo.setDescription(productForm.getDescription());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setName(productForm.getName());
        productPojo.setClientSkuId(productForm.getClientSkuId());
        return productPojo;
    }


    public static ProductPojo convertProductUpdateFormToPojo(ProductUpdateForm productUpdateForm, Long globalSkuId, Long clientId) {
        if (isNull(productUpdateForm))
            return new ProductPojo();

        ProductPojo productPojo = new ProductPojo();
        productPojo.setGlobalSkuId(globalSkuId);
        productPojo.setMrp(productUpdateForm.getMrp());
        productPojo.setName(productUpdateForm.getName());
        productPojo.setDescription(productUpdateForm.getDescription());
        productPojo.setClientId(clientId);
        productPojo.setBrandId(productUpdateForm.getBrandId());

        return productPojo;
    }

    public static BinData convertBinPojoToData(BinPojo binPojo) {
        if (isNull(binPojo))
            return new BinData();

        BinData binData = new BinData();
        binData.setId(binPojo.getBinId());

        return binData;
    }

    public static List<BinData> convertBinPojoListToData(List<BinPojo> binPojoList) {
        if (CollectionUtils.isEmpty(binPojoList))
            return new ArrayList<>();

        List<BinData> binDataList = new ArrayList<>();
        for (BinPojo binPojo : binPojoList) {
            binDataList.add(convertBinPojoToData(binPojo));
        }
        return binDataList;
    }

    public static BinSkuPojo convertBinSkuFormToPojo(BinSkuItemForm binSkuItemForm, Long globalSkuId) {
        if (isNull(binSkuItemForm))
            return new BinSkuPojo();

        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setBinId(binSkuItemForm.getBinId());
        binSkuPojo.setGlobalSkuId(globalSkuId);
        binSkuPojo.setQuantity(binSkuItemForm.getQuantity());

        return binSkuPojo;

    }

    public static List<BinSkuPojo> convertListBinSkuFormToPojo(List<BinSkuItemForm> binSkuItemFormList,
                                                               Map<String, Long> clientToGlobalSkuIdMap) {
        if (CollectionUtils.isEmpty(binSkuItemFormList))
            return new ArrayList<>();

        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        for (BinSkuItemForm binSkuItemForm : binSkuItemFormList)
            binSkuPojoList.add(convertBinSkuFormToPojo(binSkuItemForm, clientToGlobalSkuIdMap.get(binSkuItemForm.getClientSkuId())));

        return binSkuPojoList;
    }


    public static BinSkuPojo convertBinSkuUpdateFormToPojo(BinSkuUpdateForm binSkuUpdateForm, Long id) {
        if (isNull(binSkuUpdateForm))
            return new BinSkuPojo();

        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setQuantity(binSkuUpdateForm.getQuantity());
        binSkuPojo.setId(id);

        return binSkuPojo;
    }

    public static ChannelData convertChannelPojoToData(ChannelPojo channelPojo) {
        if (isNull(channelPojo))
            return new ChannelData();

        ChannelData channelData = new ChannelData();
        channelData.setName(channelPojo.getName());
        channelData.setInvoiceTypes(channelPojo.getInvoiceType());
        channelData.setId(channelPojo.getId());

        return channelData;
    }

    public static List<ChannelData> convertChannelPojoListToData(List<ChannelPojo> channelPojoList) {
        if (CollectionUtils.isEmpty(channelPojoList))
            return new ArrayList<>();

        List<ChannelData> channelDataList = new ArrayList<>();
        for (ChannelPojo channelPojo : channelPojoList) {
            channelDataList.add(convertChannelPojoToData(channelPojo));
        }

        return channelDataList;
    }

    public static ChannelPojo convertChannelFormToPojo(ChannelForm channelForm) {
        if (isNull(channelForm))
            return new ChannelPojo();

        ChannelPojo channelPojo = new ChannelPojo();
        channelPojo.setName(channelForm.getName());
        channelPojo.setInvoiceType(channelForm.getInvoiceType());

        return channelPojo;
    }

    public static void checkDuplicateChannelListingFormList(List<ChannelListingForm> channelListingFormList) throws ApiException {
        if (CollectionUtils.isEmpty(channelListingFormList))
            throw new ApiException("Empty form list");

        HashSet<String> setChannelSkuId = new HashSet<>();
        HashSet<String> setClientSkuId = new HashSet<>();
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;

        for (ChannelListingForm channelListingForm : channelListingFormList) { //TODO split in 2 for
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

    public static void checkDuplicateClientSkuIds(List<OrderItemForm> orderItemFormList) throws ApiException {
        if (CollectionUtils.isEmpty(orderItemFormList))
            throw new ApiException("Empty form list");

        Set<String> clientSkuIdSet = new HashSet<>();
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;

        for (OrderItemForm orderItemForm : orderItemFormList) {
            if (clientSkuIdSet.contains(orderItemForm.getClientSkuId())) {
                errorFormList.add(new ErrorData(row, "Duplicate client sku id"));
            }
            clientSkuIdSet.add(orderItemForm.getClientSkuId());
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }

    public static void checkDuplicateChannelSkuIds(List<OrderItemFormChannel> orderItemFormChannelList) throws ApiException {
        if (CollectionUtils.isEmpty(orderItemFormChannelList))
            throw new ApiException("Empty form list");

        Set<String> clientSkuIdSet = new HashSet<>();
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;

        for (OrderItemFormChannel orderItemFormChannel : orderItemFormChannelList) {
            if (clientSkuIdSet.contains(orderItemFormChannel.getChannelSkuId())) {
                errorFormList.add(new ErrorData(row, "Duplicate client sku id"));
            }
            clientSkuIdSet.add(orderItemFormChannel.getChannelSkuId());
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }

    public static OrderPojo convertOrderFormToOrderPojo(OrderForm orderForm, Long channelId) {
        if (isNull(orderForm))
            return new OrderPojo();

        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setChannelOrderId(orderForm.getChannelOrderId());
        orderPojo.setCustomerId(orderForm.getCustomerId());
        orderPojo.setClientId(orderForm.getClientId());
        orderPojo.setChannelId(channelId);

        return orderPojo;
    }

    public static OrderPojo convertOrderFormChannelToOrderPojo(ChannelOrderForm channelOrderForm, Long channelId) {
        if (isNull(channelOrderForm))
            return new OrderPojo();

        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setChannelOrderId(channelOrderForm.getChannelOrderId());
        orderPojo.setCustomerId(channelOrderForm.getCustomerId());
        orderPojo.setClientId(channelOrderForm.getClientId());
        orderPojo.setChannelId(channelId);

        return orderPojo;
    }

    public static List<OrderItemPojo> convertOrderItemListToOrderItemPojo(List<OrderItemForm> orderItemFormList,
                                                                          Map<String, Long> clientSkuIdToGlobalSkuIdMap) {
        if (CollectionUtils.isEmpty(orderItemFormList))
            return new ArrayList<>();

        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItemFormList) {
            OrderItemPojo orderItemPojo = new OrderItemPojo();
            orderItemPojo.setGlobalSkuId(clientSkuIdToGlobalSkuIdMap.get(orderItemForm.getClientSkuId()));
            orderItemPojo.setOrderedQuantity(orderItemForm.getQuantity());
            orderItemPojo.setSellingPricePerUnit(orderItemForm.getSellingPricePerUnit());

            orderItemPojoList.add(orderItemPojo);
        }
        return orderItemPojoList;
    }

    public static List<OrderItemPojo> convertOrderItemListChannelToOrderItemPojo(List<OrderItemFormChannel> orderItemFormChannelList,
                                                                                 Map<String, Long> channelSkuIdToGlobalSkuIdMap) {
        if (CollectionUtils.isEmpty(orderItemFormChannelList))
            return new ArrayList<>();

        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for (OrderItemFormChannel orderItemFormChannel : orderItemFormChannelList) {
            OrderItemPojo orderItemPojo = new OrderItemPojo();
            orderItemPojo.setGlobalSkuId(channelSkuIdToGlobalSkuIdMap.get(orderItemFormChannel.getChannelSkuId()));
            orderItemPojo.setOrderedQuantity(orderItemFormChannel.getQuantity());
            orderItemPojo.setSellingPricePerUnit(orderItemFormChannel.getSellingPricePerUnit());

            orderItemPojoList.add(orderItemPojo);
        }
        return orderItemPojoList;
    }

    public static List<InventoryPojo> convertListBinSkuFormToInventoryPojo(List<BinSkuPojo> binSkuPojoList) {
        if (CollectionUtils.isEmpty(binSkuPojoList))
            return new ArrayList<>();

        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        Map<Long, Long> GSkuIdToQuantityMap = binSkuPojoList.stream()
                .collect(groupingBy(BinSkuPojo::getGlobalSkuId, summingLong(BinSkuPojo::getQuantity)));
        for (Long gSku : GSkuIdToQuantityMap.keySet()) {
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setAvailableQuantity(GSkuIdToQuantityMap.get(gSku));
            inventoryPojo.setGlobalSkuId(gSku);

            inventoryPojoList.add(inventoryPojo);
        }
        return inventoryPojoList;
    }

    public static List<InventoryPojo> convertBinSkuUpdateFormToInventoryPojo(BinSkuUpdateForm binSkuUpdateForm, Pair<Long, Long> dataPair) {
        if (isNull(binSkuUpdateForm))
            return new ArrayList<>();

        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setAvailableQuantity(dataPair.getKey() - binSkuUpdateForm.getQuantity());
        inventoryPojo.setGlobalSkuId(dataPair.getValue());
        inventoryPojoList.add(inventoryPojo);
        return inventoryPojoList;
    }

    public static OrderItemInvoiceData convertPojoOrderItemToData(OrderItemPojo orderItemPojo, String clientSkuId, String channelOrderId) {
        if (isNull(orderItemPojo))
            return new OrderItemInvoiceData();

        OrderItemInvoiceData orderItemInvoiceData = new OrderItemInvoiceData();
        orderItemInvoiceData.setChannelOrderId(channelOrderId);
        orderItemInvoiceData.setOrderedQuantity(orderItemPojo.getOrderedQuantity());
        orderItemInvoiceData.setClientSkuId(clientSkuId);
        orderItemInvoiceData.setSellingPricePerUnit(orderItemPojo.getSellingPricePerUnit());

        return orderItemInvoiceData;
    }

    public static OrderItemChannelData convertPojoOrderItemChannelToData(OrderItemPojo orderItemPojo,
                                                                         String channelSkuId, String channelOrderId) {
        if (isNull(orderItemPojo))
            return new OrderItemChannelData();

        OrderItemChannelData orderItemChannelData = new OrderItemChannelData();
        orderItemChannelData.setChannelOrderId(channelOrderId);
        orderItemChannelData.setOrderedQuantity(orderItemPojo.getOrderedQuantity());
        orderItemChannelData.setChannelSkuId(channelSkuId);
        orderItemChannelData.setSellingPricePerUnit(orderItemPojo.getSellingPricePerUnit());

        return orderItemChannelData;
    }

    public static ChannelListingData convertChannelListingPojoToData(ChannelListingPojo channelListingPojo,
                                                                     String channelName, String clientName) {
        if (isNull(channelListingPojo))
            return new ChannelListingData();

        ChannelListingData channelListingData = new ChannelListingData();
        channelListingData.setChannelName(channelName);
        channelListingData.setChannelSkuId(channelListingPojo.getChannelSkuId());
        channelListingData.setId(channelListingPojo.getId());
        channelListingData.setGlobalSkuId(channelListingPojo.getGlobalSkuId());
        channelListingData.setClientName(clientName);
        return channelListingData;
    }

    public static OrderData convertOrderPojoToData(OrderPojo orderPojo, String channelName, String clientName,
                                                   String customerName) {
        if (isNull(orderPojo))
            return new OrderData();

        OrderData orderData = new OrderData();
        orderData.setChannelOrderId(orderPojo.getChannelOrderId());
        orderData.setInvoiceUrl(orderPojo.getInvoiceUrl());
        orderData.setChannelName(channelName);
        orderData.setClientName(clientName);
        orderData.setCustomerName(customerName);
        orderData.setStatus(orderPojo.getStatus());
        orderData.setId(orderPojo.getId());

        return orderData;
    }

    public static OrderItemData convertOrderItemPojToData(OrderItemPojo orderItemPojo, String clientSkuId, String channelSkuId) {
        if (isNull(orderItemPojo))
            return new OrderItemData();

        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setOrderId(orderItemPojo.getOrderId());
        orderItemData.setId(orderItemPojo.getId());
        orderItemData.setSellingPricePerUnit(orderItemPojo.getSellingPricePerUnit());
        orderItemData.setOrderedQuantity(orderItemPojo.getOrderedQuantity());
        orderItemData.setAllocatedQuantity(orderItemPojo.getAllocatedQuantity());
        orderItemData.setFulfilledQuantity(orderItemPojo.getFulfilledQuantity());
        orderItemData.setClientSkuId(clientSkuId);
        orderItemData.setChannelSkuId(channelSkuId);
        return orderItemData;
    }

}

