package assure.util;

import assure.spring.ApiException;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class DataUtil {
    private static final String INVOICE_STORAGE_LOCATION = "src/invoice";
    private static final String STRING_JOIN_DELIMITER = "_@!#$(!";

    public static byte[] returnFileStream(String url) throws ApiException {
        String fileName = url.split("/")[url.split("/").length-1];
        File file = new File( INVOICE_STORAGE_LOCATION,fileName);
        if (file.exists()) {
            try {
                byte[] pdf = Files.readAllBytes(file.toPath());
                return pdf;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new ApiException("Invoice not found at path");
    }

    public static <T> String getKey(List<Object> list) {
        if(CollectionUtils.isEmpty(list))
            return null;

        List<String> strings = new ArrayList<>();
        for (Object o : list) {
            strings.add(o.toString());
        }
        return String.join(STRING_JOIN_DELIMITER, strings);
    }


}

