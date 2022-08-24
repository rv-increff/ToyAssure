package assure.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class BinSkuItemForm {
    @NotNull
    private Long binId;
    @NotNull
    @PositiveOrZero
    private Long quantity;
    @NotBlank
    private String clientSkuId;
}
