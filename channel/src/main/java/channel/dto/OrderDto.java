package channel.dto;

import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import requests.Requests;
import requests.Requests.*;

@Service
public class OrderDto {

    public String get() throws Exception {
        Requests.get("http://localhost:9000/assure/bins?pageNumber=0");
return "";
    }

}
