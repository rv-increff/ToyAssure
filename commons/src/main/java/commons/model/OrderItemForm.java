package commons.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class OrderItemForm {

    @NotBlank
    private String clientSkuId;

    @NotNull
    private Long quantity;

    @NotNull
    private Double sellingPricePerUnit;

}
