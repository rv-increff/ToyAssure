package assure.controller;

import assure.dto.OrderDto;
import assure.model.BinData;
import assure.model.OrderItemForm;
import assure.spring.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class OrderController {

    @Autowired
    OrderDto orderDto;

    @ApiOperation(value = "Create order")
    @RequestMapping(path = "/orders", method = RequestMethod.POST)
    public Integer addOrder(@RequestParam(name = "clientName") String clientName,
                                 @RequestParam(name = "channelName") String channelName,
                                 @RequestParam(name = "customerName") String customerName,
                                 List<OrderItemForm> orderItemFormList) throws ApiException {
        return orderDto.add(clientName,channelName,customerName,orderItemFormList);
    }
}
