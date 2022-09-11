package channel.dto;

import commons.requests.Requests;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static commons.requests.Requests.objectToJsonString;
@Service
public class AssureClient {
    @Value("${assure_base_url}")
    private String assureBaseUrl;

    public String post(String url, Object obj) throws Exception {
        return Requests.post(assureBaseUrl + url, objectToJsonString(obj));
    }
}
