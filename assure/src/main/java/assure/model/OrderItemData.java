package assure.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class OrderItemData {
    private Long orderId;
    private String clientSkuId;
    private Long orderedQuantity;
    private Double sellingPricePerUnit;
}
