package assure.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class OrderItemForm {

    @NotBlank
    private String clientSkuId;

    @Min(value = 0)
    private Long quantity;

    @Min(value = 0)
    private Double sellingPricePerUnit;

}
