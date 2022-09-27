package channel.dto;

import commons.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static channel.controller.RestPath.*;
import static channel.util.HelperUtil.returnFileStream;
import static commons.util.pdfUtil.convertToPDF;
import static commons.util.pdfUtil.jaxbObjectToXML;

@Service
public class AssureProxyDto {
    private static final String INVOICE_STORAGE_LOCATION = "src/invoice";
    private static final String INVOICE_TEMPLATE_LOCATION = "src/invoiceTemplate";
    private static final String INVOICE_XSL_NAME = "invoice.xsl";
    private static final String INVOICE_NAME = "invoice.pdf";


    @Autowired
    AssureClient assureClient;
    public String add(ChannelOrderForm channelOrderForm) throws Exception {
        return assureClient.post(ASSURE_CHANNEL_ORDER_URL, channelOrderForm);
    }

    public byte[] getInvoice(InvoiceDataChannel invoiceDataChannel) throws IOException, TransformerException {
        String xml = jaxbObjectToXML(invoiceDataChannel, InvoiceDataChannel.class);
        File xsltFile = new File(INVOICE_TEMPLATE_LOCATION, INVOICE_XSL_NAME); //TODO make all private static final done
        File pdfFile = new File(INVOICE_STORAGE_LOCATION, INVOICE_NAME);
        convertToPDF(invoiceDataChannel, xsltFile, pdfFile, xml);
        return returnFileStream();
    }
    public List<OrderData> selectOrder(Integer pageNumber) throws Exception {
        String url =  ASSURE_SELECT_ORDER_URL + pageNumber; //TODO move all the url to new Class rest path done
        return assureClient.getList(url,OrderData.class);
    }

    public List<OrderItemData> selectOrderItems(Long orderId) throws Exception {
        String url = String.format(ASSURE_SELECT_ORDER_ITEM_URL, orderId);
        return assureClient.getList(url, OrderItemData.class);
    }
    public List<PartyData> partySearch(PartySearchForm partySearchForm) throws Exception {
        return assureClient.postForList(ASSURE_PARTY_SEARCH_URL, partySearchForm, PartyData.class);
    }

    public List<ChannelData> selectChannel() throws Exception {
        return assureClient.getList(ASSURE_SELECT_CHANNEL_URL, ChannelData.class);
    }

    public List<ChannelListingData> selectChannelListing(Long channelId, Long clientId) throws Exception {
        String url = String.format(ASSURE_SELECT_CHANNEL_LISTING_URL, channelId, clientId);
        return assureClient.getList(url, ChannelListingData.class);
    }

}
