package assure.util;

import assure.model.*;
import assure.model.OrderForm;
import assure.pojo.*;
import assure.spring.ApiException;
import commons.model.*;

import java.util.*;

import static assure.util.ValidationUtil.throwErrorIfNotEmpty;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

public class ConversionUtil {
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

    public static BinSkuPojo convertBinSkuFormToPojo(BinSkuItemForm binSkuItemForm, Long globalSkuId) {

        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setBinId(binSkuItemForm.getBinId());
        binSkuPojo.setGlobalSkuId(globalSkuId);
        binSkuPojo.setQuantity(binSkuItemForm.getQuantity());

        return binSkuPojo;

    }

    public static List<BinSkuPojo> convertListBinSkuFormToPojo(List<BinSkuItemForm> binSkuItemFormList,
                                                               HashMap<String, Long> clientToGlobalSkuIdMap) {
        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        for (BinSkuItemForm binSkuItemForm : binSkuItemFormList) {
            binSkuPojoList.add(convertBinSkuFormToPojo(binSkuItemForm, clientToGlobalSkuIdMap.get(binSkuItemForm.getClientSkuId())));
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

    public static ChannelPojo convertChannelFormToPojo(ChannelForm channelForm) {
        ChannelPojo channelPojo = new ChannelPojo();
        channelPojo.setName(channelForm.getName());
        channelPojo.setInvoiceType(channelForm.getInvoiceType());

        return channelPojo;
    }

    public static void checkDuplicateChannelListingFormList(List<ChannelListingForm> channelListingFormList) throws ApiException {
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

    public static OrderPojo createOrderPojo(Long clientId, Long customerId, Long channelId, String channelOrderId) {
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
                                                                             Map<String, Long> clientSkuIdToGlobalSkuIdMap) {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItemFormList) {
            OrderItemPojo orderItemPojo = new OrderItemPojo();

            orderItemPojo.setOrderId(orderId);
            orderItemPojo.setOrderedQuantity(orderItemForm.getQuantity());
            orderItemPojo.setGlobalSkuId(clientSkuIdToGlobalSkuIdMap.get(orderItemForm.getClientSkuId()));
            orderItemPojo.setSellingPricePerUnit(orderItemForm.getSellingPricePerUnit());
            orderItemPojo.setAllocatedQuantity(0L); //TODO move to service
            orderItemPojo.setFulfilledQuantity(0L);

            orderItemPojoList.add(orderItemPojo);
        }
        return orderItemPojoList;
    }

    public static OrderPojo convertOrderFormToOrderPojo(OrderForm orderForm, Long channelId) {
        OrderPojo orderPojo = new OrderPojo();

        orderPojo.setChannelOrderId(orderForm.getChannelOrderId());
        orderPojo.setCustomerId(orderForm.getCustomerId());
        orderPojo.setClientId(orderForm.getClientId());
        orderPojo.setChannelId(channelId);

        return orderPojo;
    }
 public static OrderPojo convertOrderFormChannelToOrderPojo(OrderFormChannel orderFormChannel, Long channelId) {
        OrderPojo orderPojo = new OrderPojo();

        orderPojo.setChannelOrderId(orderFormChannel.getChannelOrderId());
        orderPojo.setCustomerId(orderFormChannel.getCustomerId());
        orderPojo.setClientId(orderFormChannel.getClientId());
        orderPojo.setChannelId(channelId);

        return orderPojo;
    }

    public static List<OrderItemPojo> convertOrderItemListToOrderItemPojo(List<OrderItemForm> orderItemFormList,
                                                                          Map<String, Long> clientSkuIdToGlobalSkuIdMap) {
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

    public static List<InventoryPojo> convertListBinSkuFormToInventoryPojo(List<BinSkuItemForm> binSkuItemFormList,
                                                                           HashMap<String, Long> clientToGlobalSkuIdMap) {
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        Map<String, Long> clientSkuIdToQuantityMap = binSkuItemFormList.stream()
                .collect(groupingBy(BinSkuItemForm::getClientSkuId, summingLong(BinSkuItemForm::getQuantity)));
        for (String clientSkuId : clientSkuIdToQuantityMap.keySet()) {
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setAvailableQuantity(clientSkuIdToQuantityMap.get(clientSkuId));
            inventoryPojo.setGlobalSkuId(clientToGlobalSkuIdMap.get(clientSkuId));

            inventoryPojoList.add(inventoryPojo);
        }
        return inventoryPojoList;
    }
    public static OrderItemInvoiceData convertPojoOrderItemToData(OrderItemPojo orderItemPojo, String clientSkuId, String channelOrderId){
        OrderItemInvoiceData orderItemInvoiceData = new OrderItemInvoiceData();
        orderItemInvoiceData.setChannelOrderId(channelOrderId);
        orderItemInvoiceData.setOrderedQuantity(orderItemPojo.getOrderedQuantity());
        orderItemInvoiceData.setClientSkuId(clientSkuId);
        orderItemInvoiceData.setSellingPricePerUnit(orderItemPojo.getSellingPricePerUnit());

        return orderItemInvoiceData;
    }
     public static OrderItemChannelData convertPojoOrderItemChannelToData(OrderItemPojo orderItemPojo,
                                                                          String channelSkuId, String channelOrderId){
        OrderItemChannelData orderItemChannelData = new OrderItemChannelData();
        orderItemChannelData.setChannelOrderId(channelOrderId);
        orderItemChannelData.setOrderedQuantity(orderItemPojo.getOrderedQuantity());
        orderItemChannelData.setChannelSkuId(channelSkuId);
        orderItemChannelData.setSellingPricePerUnit(orderItemPojo.getSellingPricePerUnit());

        return orderItemChannelData;
    }

    public static ChannelListingData convertChannelListingPojoToData(ChannelListingPojo channelListingPojo){
        ChannelListingData channelListingData = new ChannelListingData();
        channelListingData.setChannelId(channelListingPojo.getChannelId());
        channelListingData.setChannelSkuId(channelListingPojo.getChannelSkuId());
        channelListingData.setId(channelListingPojo.getId());
        channelListingData.setGlobalSkuId(channelListingData.getGlobalSkuId());
        channelListingData.setClientId(channelListingPojo.getClientId());
        return channelListingData;
    }

    public static List<ChannelListingData> convertChannelListingPojoListToData(List<ChannelListingPojo> channelListingPojoList){
        List<ChannelListingData> channelListingDataList = new ArrayList<>();
        for (ChannelListingPojo channelListingPojo : channelListingPojoList) {
            channelListingDataList.add(convertChannelListingPojoToData(channelListingPojo));
        }
        return channelListingDataList;
    }
    public static OrderData convertOrderPojoToData(OrderPojo orderPojo){
        OrderData orderData = new OrderData();
        orderData.setChannelOrderId(orderPojo.getChannelOrderId());
        orderData.setInvoiceUrl(orderPojo.getInvoiceUrl());
        orderData.setChannelId(orderPojo.getChannelId());
        orderData.setClientId(orderPojo.getClientId());
        orderData.setCustomerId(orderPojo.getCustomerId());
        orderData.setStatus(orderPojo.getStatus());
        orderData.setId(orderPojo.getId());

        return orderData;
    }

    public static List<OrderData> convertOrderPojoListToData(List<OrderPojo> orderPojoList){
        List<OrderData> orderDataList = new ArrayList<>();
        for (OrderPojo orderPojo : orderPojoList) {
            orderDataList.add(convertOrderPojoToData(orderPojo));
        }
        return orderDataList;
    }
}

