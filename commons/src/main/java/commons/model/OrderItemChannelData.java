package commons.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemChannelData {
    private String channelOrderId;
    private String channelSkuId;
    private Long orderedQuantity;
    private Double sellingPricePerUnit;
}
