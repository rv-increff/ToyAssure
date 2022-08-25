package channel.dto;

import commons.model.OrderForm;
import commons.requests.Requests;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
@Service
public class OrderDto {
    public String add(OrderForm orderForm) throws Exception {

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(orderForm);
        return Requests.post("http://localhost:9000/assure/orders", json);
    }

}
