package channel.dto;

import channel.spring.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import commons.requests.Requests;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static channel.util.HelperUtil.stringToObject;
import static channel.util.HelperUtil.stringToObjectList;
import static commons.requests.Requests.objectToJsonString;

@Service
public class AssureClient {
    @Value("${assure_base_url}")
    private String assureBaseUrl;

    public String post(String url, Object obj) throws Exception {
        String response = Requests.post(assureBaseUrl + url, objectToJsonString(obj));
        checkNotException(response);
        return response;
    }

    public <T> List<T> postForList(String url, Object obj, Class<T> clazz) throws Exception {
        String response = Requests.post(assureBaseUrl + url, objectToJsonString(obj));
        checkNotException(response);
        return stringToObjectList(response, clazz);
    }

    public <T> T get(String url, Class<T> clazz) throws Exception {
        String response = Requests.get(assureBaseUrl + url);
        checkNotException(response);
        return stringToObject(response, clazz);
    }

    public <T> List<T> getList(String url, Class<T> clazz) throws Exception {
        String response = Requests.get(assureBaseUrl + url);
        checkNotException(response);
        return stringToObjectList(response, clazz);
    }

    public void checkNotException(String json) throws IOException, ApiException {
        try {
            HashMap map = new ObjectMapper().readValue(json, HashMap.class);
            if ((Integer) map.get("code") != 200) //TODO private static HTTP status ok
                throw new ApiException((String) map.get("description"));
        } catch (MismatchedInputException m) {

        }
    }


}
