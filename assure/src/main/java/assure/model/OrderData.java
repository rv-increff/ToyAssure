package assure.model;

import assure.util.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderData {
    private Long id;
    private Long clientId;
    private Long customerId;
    private Long channelId;
    private String channelOrderId;
    private OrderStatus status;
    private String invoiceUrl;
}
