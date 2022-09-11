package assure.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import commons.requests.Requests;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static commons.requests.Requests.objectToJsonString;
@Service
public class ChannelClient {

    @Value("${channel_base_url}")
    private String channelBaseUrl;

    public String post(String url, Object obj) throws Exception {
        return Requests.post(channelBaseUrl + url, objectToJsonString(obj));
    }
}
