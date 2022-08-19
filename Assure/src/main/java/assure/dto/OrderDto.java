package assure.dto;

import assure.model.ErrorData;
import assure.model.OrderItemForm;
import assure.pojo.ChannelPojo;
import assure.pojo.OrderPojo;
import assure.pojo.PartyPojo;
import assure.pojo.ProductPojo;
import assure.service.*;
import assure.spring.ApiException;
import assure.util.Helper;
import assure.util.PartyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static assure.util.Helper.*;
import static java.util.Objects.isNull;

@Service
public class OrderDto {
    private static final Long MAX_LIST_SIZE = 1000L;

    @Autowired
    private OrderService orderService;
    @Autowired
    private PartyService partyService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderItemService orderItemService;


    public Integer add(String clientName, String channelOrderId, String customerName, List<OrderItemForm> orderItemFormList)
            throws ApiException {

        if (orderItemFormList.size() > MAX_LIST_SIZE) {
            throw new ApiException("List size more than limit, limit : " + MAX_LIST_SIZE);
        }
        Helper.validateList("order Item List", orderItemFormList);
        checkDuplicatesOrderItemFormList(orderItemFormList);

        PartyPojo partyPojo = partyService.selectByNameAndPartyType(clientName, PartyType.CLIENT);
        if (isNull(partyPojo)) {
            throw new ApiException("client does not exist");
        }
        Long clientId = partyPojo.getId();
        partyPojo = partyService.selectByNameAndPartyType(customerName, PartyType.CUSTOMER);
        if (isNull(partyPojo)) {
            throw new ApiException("customer does not exist");
        }
        Long customerId = partyPojo.getId();
        ChannelPojo channelPojo = channelService.selectByName("INTERNAL");
        if (isNull(channelPojo)) {
            throw new ApiException("INTERNAL channel does not exists");
        }
        Long channelId = channelPojo.getId();

        Map<String, Long> clientSkuIdToGlobalSkuIdMap = getCheckClientSkuId(orderItemFormList);
        OrderPojo orderPojo = orderService.add(createOrderPojo(clientId, customerId, channelId, channelOrderId));
        orderItemService.add(transformAndConvertOrderItemFormToPojo(orderPojo.getId(), orderItemFormList,
                clientSkuIdToGlobalSkuIdMap));

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

}
