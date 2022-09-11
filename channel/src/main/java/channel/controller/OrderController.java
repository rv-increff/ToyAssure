package channel.controller;

import channel.dto.OrderDto;
import commons.model.InvoiceDataChannel;
import commons.model.ChannelOrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class OrderController {

    @Autowired
    OrderDto orderDto;

    @ApiOperation(value = "Create order")
    @RequestMapping(path = "/orders", method = RequestMethod.POST)
    public String addOrder(@RequestBody ChannelOrderForm channelOrderForm) throws Exception {
        return orderDto.add(channelOrderForm);
    }

    @ApiOperation(value = "Get invoice")
    @RequestMapping(path = "/orders/get-invoice", method = RequestMethod.POST)
    public byte[] getInvoice(@RequestBody InvoiceDataChannel invoiceDataChannel) throws Exception {
        return orderDto.getInvoice(invoiceDataChannel);
    }

}
