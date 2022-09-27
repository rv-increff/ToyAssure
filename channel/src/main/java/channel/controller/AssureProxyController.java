package channel.controller;

import channel.dto.AssureProxyDto;
import commons.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class AssureProxyController {

    @Autowired
    private AssureProxyDto assureProxyDto;

    @ApiOperation(value = "Create order")
    @RequestMapping(path = "/orders", method = RequestMethod.POST)
    public String addOrder(@RequestBody ChannelOrderForm channelOrderForm) throws Exception {
        return assureProxyDto.add(channelOrderForm);
    }

    @ApiOperation(value = "Get invoice")
    @RequestMapping(path = "/orders/get-invoice", method = RequestMethod.POST)
    public byte[] getInvoice(@RequestBody InvoiceDataChannel invoiceDataChannel) throws Exception {
        return assureProxyDto.getInvoice(invoiceDataChannel);
    }

    @ApiOperation(value = "Get orders")
    @RequestMapping(path = "/orders", method = RequestMethod.GET)
    public List<OrderData> getOrders(@RequestParam Integer pageNumber) throws Exception {
        return assureProxyDto.selectOrder(pageNumber);
    }

    @ApiOperation(value = "Get orders items")
    @RequestMapping(path = "/orders/{orderId}/order-items", method = RequestMethod.GET)
    public List<OrderItemData> getOrderItems(@PathVariable Long orderId) throws Exception {
        return assureProxyDto.selectOrderItems(orderId);
    }

    @ApiOperation(value = "Search Party")
    @RequestMapping(path = "/parties/search", method = RequestMethod.POST)
    public List<PartyData> searchParty(@RequestBody PartySearchForm partySearchForm) throws Exception {
        return assureProxyDto.partySearch(partySearchForm);
    }

    @ApiOperation(value = "Get Channels")
    @RequestMapping(path = "/channels", method = RequestMethod.GET)
    public List<ChannelData> getChannels() throws Exception {
        return assureProxyDto.selectChannel();
    }

    @ApiOperation(value = "Get ChannelListings")
    @RequestMapping(path = "/channel-listings", method = RequestMethod.GET)
    public List<ChannelListingData> getChannelListings(@RequestParam Long channelId,
                                                       @RequestParam Long clientId) throws Exception {
        return assureProxyDto.selectChannelListing(channelId, clientId);

    }
}
