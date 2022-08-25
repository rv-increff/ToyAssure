package channel.controller;

import channel.dto.OrderDto;
import channel.spring.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.TransformerException;
import java.io.IOException;
@Api
@RestController
public class OrderController {

    @Autowired
    OrderDto orderDto;

    @ApiOperation(value = "Get invoice")
    @RequestMapping(path = "/get", method = RequestMethod.GET) //TODO /orders   /order/upload-order
    public String getInvoice() throws Exception {
        return orderDto.get();
    }




}
