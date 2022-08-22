package assure.controller;

import assure.dto.OrderDto;
import assure.model.BinData;
import assure.model.OrderItemForm;
import assure.spring.ApiException;
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
    @RequestMapping(path = "/orders", method = RequestMethod.POST) //TODO /orders   /order/upload-order
    public Integer addOrder(@RequestParam(name = "clientName") String clientName, //TODO take ids not name
                                 @RequestParam(name = "channelOrderId") String channelOrderId, //TODO make it as whole object
                                 @RequestParam(name = "customerName") String customerName,
                                 @RequestBody List<OrderItemForm> orderItemFormList) throws ApiException {
        return orderDto.add(clientName,channelOrderId,customerName,orderItemFormList);
    }
}
