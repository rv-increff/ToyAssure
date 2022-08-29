package assure.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemInvoiceData {
    private String channelOrderId;
    private String clientSkuId;
    private Long orderedQuantity;
    private Double sellingPricePerUnit;
}
