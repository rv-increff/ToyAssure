package channel.dto;

import commons.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static channel.util.HelperUtil.returnFileStream;
import static commons.util.pdfUtil.convertToPDF;
import static commons.util.pdfUtil.jaxbObjectToXML;

@Service
public class AssureProxyDto {
    @Autowired
    AssureClient assureClient;
    public String add(ChannelOrderForm channelOrderForm) throws Exception {
        return assureClient.post("orders/channel-orders", channelOrderForm);
    }

    public byte[] getInvoice(InvoiceDataChannel invoiceDataChannel) throws IOException, TransformerException {
        String xml = jaxbObjectToXML(invoiceDataChannel, InvoiceDataChannel.class);
        File xsltFile = new File("src", "invoice.xsl");
        File pdfFile = new File("src", "invoice.pdf");
        convertToPDF(invoiceDataChannel, xsltFile, pdfFile, xml);
        return returnFileStream();
    }
    public List<OrderData> selectOrder(Integer pageNumber) throws Exception {
        String url = "orders?invoiceType=CHANNEL&pageNumber=" + pageNumber;
        return assureClient.getList(url,OrderData.class);
    }

    public List<OrderItemData> selectOrderItems(Long orderId) throws Exception {
        String url = "orders/" + orderId  + "/order-items";
        return assureClient.getList(url, OrderItemData.class);
    }
    public List<PartyData> partySearch(PartySearchForm partySearchForm) throws Exception {
        return assureClient.postForList("parties/search", partySearchForm, PartyData.class);
    }

    public List<ChannelData> selectChannel() throws Exception {
        return assureClient.getList("channels", ChannelData.class);
    }

    public List<ChannelListingData> selectChannelListing(Long channelId, Long clientId) throws Exception {
        String url = "channel-listings?channelId=" + channelId+ "&clientId=" + clientId;
        return assureClient.getList(url, ChannelListingData.class);
    }

}
