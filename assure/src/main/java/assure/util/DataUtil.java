package assure.util;

import assure.model.*;
import assure.pojo.BinSkuPojo;
import assure.spring.ApiException;
import org.apache.fop.apps.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static assure.util.ValidationUtil.throwErrorIfNotEmpty;

public class DataUtil {
    private static final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
    public static int binSkuPojoSortByQtyComparator(BinSkuPojo binSkuPojo1, BinSkuPojo binSkuPojo2){
        return (int)( binSkuPojo1.getQuantity() - binSkuPojo2.getQuantity());
    }

    public static HashMap<String, Long> checkClientSkuIdExist(HashMap<String, Long> clientToGlobalSkuIdMap, List<BinSkuItemForm> binSkuItemForms)
            throws ApiException {

        Integer row = 0;
        List<ErrorData> errorFormList = new ArrayList<>();
        for (BinSkuItemForm binSkuItemForm : binSkuItemForms) {
            if (!clientToGlobalSkuIdMap.containsKey(binSkuItemForm.getClientSkuId())) {
                errorFormList.add(new ErrorData(row, "clientSkuId does not exist, clientSkuId : "
                        + binSkuItemForm.getClientSkuId()));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
        return clientToGlobalSkuIdMap;
    }

    public static String jaxbObjectToXML(InvoiceData invoiceData) {
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(InvoiceData.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(invoiceData, sw);
            String xmlContent = sw.toString();
            return xmlContent;

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void convertToPDF(InvoiceData team, File xslt, File pdf, String xml) throws IOException, TransformerException {

        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // configure foUserAgent as desired

        // Setup output
        OutputStream out = new java.io.FileOutputStream(pdf);
        out = new java.io.BufferedOutputStream(out);
        try {
            // Construct fop with desired output format
            Fop fop = null;
            try {
                fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            } catch (FOPException e) {
                throw new RuntimeException(e);
            }

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslt));

            // Setup input for XSLT transformation
            Source src = new StreamSource(new StringReader(xml));
//                        Source src = new StreamSource(new FileInputStream(xml));
//
            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            transformer.transform(src, res);
        } catch (FOPException e) {
            throw new RuntimeException(e);
        } finally {
            out.close();
        }
    }

}

