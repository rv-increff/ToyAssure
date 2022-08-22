package assure.dto;

import assure.model.ErrorData;
import assure.model.OrderForm;
import assure.model.OrderItemForm;
import assure.pojo.ChannelPojo;
import assure.pojo.OrderItemPojo;
import assure.pojo.OrderPojo;
import assure.pojo.ProductPojo;
import assure.service.*;
import assure.spring.ApiException;
import assure.util.ConversionUtil;
import assure.util.PartyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static assure.util.ConversionUtil.*;
import static assure.util.ValidationUtil.throwErrorIfNotEmpty;
import static assure.util.ValidationUtil.validateList;
import static java.util.Objects.isNull;

@Service
public class OrderDto {
    private static final Long MAX_LIST_SIZE = 1000L;
    private static final String INTERNAL_CHANNEL = "INTERNAL";

    @Autowired
    private OrderService orderService;
    @Autowired
    private PartyService partyService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ProductService productService;

    @Transactional//TODO remove if not nessecesary
    public Integer add(OrderForm orderForm) throws ApiException {
        List<OrderItemForm> orderItemFormList = orderForm.getOrderItemFormList();
        validateList("order Item List", orderItemFormList, MAX_LIST_SIZE);
        checkDuplicateClientSkuIds(orderItemFormList);
        //TODO club it one as function big should be done in a scroll
        Long clientId = partyService.getCheckByIdAndType(orderForm.getClientId(), PartyType.CLIENT);
        Long customerId = partyService.getCheckByIdAndType(orderForm.getClientId(), PartyType.CUSTOMER);

        ChannelPojo channelPojo = channelService.selectByName(INTERNAL_CHANNEL);
        if (isNull(channelPojo)) {
            throw new ApiException(INTERNAL_CHANNEL + " channel does not exists");
        }
        Long channelId = channelPojo.getId();
        String channelOrderId = orderForm.getChannelOrderId();
        checkChannelIdAndChannelOrderIdPairNotExist(channelId, channelOrderId);
        //TODO create different
        // normalize and set there will different names no general is null check no hard coding or passing variable names

        Map<String, Long> clientSkuIdToGlobalSkuIdMap = getCheckClientSkuId(orderItemFormList);
        OrderPojo orderPojo= convertOrderFormToOrderPojo(orderForm);
        List<OrderItemPojo>  orderItemPojoList = convertOrderFormToOrderItemPojo(orderForm.getOrderItemFormList(),clientSkuIdToGlobalSkuIdMap);
        orderService.add(orderPojo,orderItemPojoList);
        //TODO remove orderiTem service

        return orderItemFormList.size();
    }

    private Map<String, Long> getCheckClientSkuId(List<OrderItemForm> orderItemFormList) throws ApiException {
        Map<String, Long> clientSkuIdToGlobalSkuIdMap = new HashMap<>();

        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (OrderItemForm orderItemForm : orderItemFormList) {
            ProductPojo productPojo = productService.selectByClientSkuId(orderItemForm.getClientSkuId());
            if (isNull(productPojo)) {
                errorFormList.add(new ErrorData(row, "clientSkuID does not exists"));
                continue;
            }
            clientSkuIdToGlobalSkuIdMap.put(orderItemForm.getClientSkuId(), productPojo.getGlobalSkuId());

            row++;
        }
        throwErrorIfNotEmpty(errorFormList);

        return clientSkuIdToGlobalSkuIdMap;
    }


    private void checkChannelIdAndChannelOrderIdPairNotExist(Long channelId, String channelOrderId) throws ApiException {
        if (!isNull(orderService.selectByChannelIdAndChannelOrderId(channelId, channelOrderId))) {
            throw new ApiException("channel order id exists for the channel");
        }
    }
}