package assure.util;

import assure.model.*;
import assure.pojo.BinSkuPojo;
import assure.spring.ApiException;
import commons.model.ErrorData;
import org.apache.fop.apps.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import static assure.util.ValidationUtil.throwErrorIfNotEmpty;

public class DataUtil {

    public static HashMap<String, Long> checkClientSkuIdExist(HashMap<String, Long> clientToGlobalSkuIdMap,
                                                              List<BinSkuItemForm> binSkuItemForms)
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

    public static byte[] returnFileStream(String url) throws URISyntaxException {
        String fileName = url.split("/")[url.split("/").length-1];
        File file = new File( "src",fileName);
        if (file.exists()) {
            try {
                byte[] pdf = Files.readAllBytes(file.toPath());
                return pdf;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}

