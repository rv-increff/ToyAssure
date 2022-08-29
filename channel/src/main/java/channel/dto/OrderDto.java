package channel.dto;

import commons.model.InvoiceDataChannel;
import commons.model.OrderFormChannel;
import commons.requests.Requests;
import org.springframework.stereotype.Service;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

import static channel.util.HelperUtil.returnFileStream;
import static commons.util.pdfUtil.convertToPDF;
import static commons.util.pdfUtil.jaxbObjectToXML;

@Service
public class OrderDto {
    public String add(OrderFormChannel orderFormChannel) throws Exception {
        return Requests.post("http://localhost:9000/assure/orders/channel-orders", Requests.objectToJsonString(orderFormChannel));
    }

    public byte[] getInvoice(InvoiceDataChannel invoiceDataChannel) throws IOException, TransformerException {
        String xml = jaxbObjectToXML(invoiceDataChannel, InvoiceDataChannel.class);
        File xsltFile = new File("src", "invoice.xsl");
        File pdfFile = new File("src", "invoice.pdf");
        convertToPDF(invoiceDataChannel, xsltFile, pdfFile, xml);
        return returnFileStream();
    }

}
