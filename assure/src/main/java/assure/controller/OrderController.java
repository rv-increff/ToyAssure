package assure.controller;

import assure.dto.OrderDto;
import assure.model.*;
import assure.spring.ApiException;
import commons.model.OrderFormChannel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Integer addChannelOrder(@RequestBody OrderFormChannel orderFormChannel) throws ApiException {
        return orderDto.addChannelOrder(orderFormChannel);
    }
    @ApiOperation(value = "Update order status")
    @RequestMapping(path = "/orders", method = RequestMethod.PATCH) //TODO /orders   /order/upload-order
    public OrderStatusUpdateForm updateStatus(@RequestBody OrderStatusUpdateForm orderStatusUpdateForm) throws ApiException {
        return orderDto.updateStatus(orderStatusUpdateForm);
    }

    @ApiOperation(value = "Get invoice")
    @RequestMapping(path = "/orders/{orderId}/get-invoice", method = RequestMethod.GET) //TODO /orders   /order/upload-order
    public byte[] getInvoice(@PathVariable Long orderId) throws Exception {
        return orderDto.getInvoice(orderId);
    }

    @ApiOperation(value = "Get orders")
    @RequestMapping(path = "/orders", method = RequestMethod.GET)
    public List<OrderData> getOrders(@RequestParam(name = "pageNumber") Integer pageNumber) {
        return orderDto.selectOrder(pageNumber);
    }

    @ApiOperation(value = "Get orders items")
    @RequestMapping(path = "/orders/{orderId}/order-items", method = RequestMethod.GET)
    public List<OrderItemData> getOrderItems(@PathVariable Long orderId) {
        return orderDto.selectOrderItems(orderId);
    }


}
