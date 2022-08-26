package channel.controller;

import channel.dto.OrderDto;
import commons.model.OrderFormChannel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import commons.model.OrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
public class OrderController {

    @Autowired
    OrderDto orderDto;

    @ApiOperation(value = "Create order")
    @RequestMapping(path = "/orders", method = RequestMethod.POST) //TODO /orders   /order/upload-order
    public String addOrder(@RequestBody OrderFormChannel orderFormChannel) throws Exception {
        return orderDto.add(orderFormChannel);
    }





}