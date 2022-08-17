package assure.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BinSkuForm {
    @NotNull
    private Long binId;
    @NotNull
    @Min(value = 0)
    private Long quantity;
    @NotBlank
    private String clientSkuId;
}
