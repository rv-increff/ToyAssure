package assure.controller;

import assure.dto.OrderDto;
import assure.model.OrderData;
import assure.model.OrderForm;
import assure.model.OrderItemData;
import assure.model.OrderStatusUpdateForm;
import assure.spring.ApiException;
import assure.util.InvoiceType;
import commons.model.ChannelOrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Api
@RestController
public class OrderController {

    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Create order")
    @RequestMapping(path = "/orders", method = RequestMethod.POST)
    public Integer addOrder(@RequestBody OrderForm orderForm) throws ApiException {
        return orderDto.add(orderForm);
    }

    @ApiOperation(value = "Create channel order")
    @RequestMapping(path = "/orders/channel-orders", method = RequestMethod.POST)
    public Integer addChannelOrder(@RequestBody ChannelOrderForm channelOrderForm) throws ApiException {
        return orderDto.addChannelOrder(channelOrderForm);
    }

    @ApiOperation(value = "Update order status")
    @RequestMapping(path = "/orders", method = RequestMethod.PATCH)
    public OrderStatusUpdateForm updateStatus(@RequestBody OrderStatusUpdateForm orderStatusUpdateForm) throws ApiException {
        return orderDto.updateStatus(orderStatusUpdateForm);
    }

    @ApiOperation(value = "Get invoice")
    @RequestMapping(path = "/orders/{orderId}/get-invoice", method = RequestMethod.GET)
    public byte[] getInvoice(@PathVariable Long orderId) throws Exception {
        return orderDto.getInvoice(orderId);
    }

    @ApiOperation(value = "Get orders items")
    @RequestMapping(path = "/orders/{orderId}/order-items", method = RequestMethod.GET)
    public List<OrderItemData> getOrderItems(@PathVariable Long orderId) {
        return orderDto.selectOrderItems(orderId);
    }

    @ApiOperation(value = "Get orders")
    @RequestMapping(path = "/orders", method = RequestMethod.GET)
    public List<OrderData> getOrders(@RequestParam(required = false) InvoiceType type, @RequestParam Integer pageNumber) {
        if (isNull(type) && !isNull(pageNumber))
            return orderDto.selectOrder(pageNumber);
        if(!isNull(pageNumber))
            return orderDto.selectOrderItemsByInvoiceType(pageNumber, type);
        return new ArrayList<>();
    }


}
