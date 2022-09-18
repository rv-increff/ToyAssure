package commons.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
@Getter
@Setter
public class OrderItemFormChannel {
    @NotBlank
    private String channelSkuId;
    @NotNull
    private Long quantity;
    @NotNull
    private Double sellingPricePerUnit;
}
