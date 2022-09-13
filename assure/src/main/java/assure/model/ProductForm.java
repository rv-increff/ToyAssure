package assure.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;


@Getter
@Setter
public class ProductForm {
    @NotBlank
    private String clientSkuId;
    @NotBlank
    private String name;
    @NotBlank
    private String brandId;
    @NotNull
    private Double mrp;
    @NotBlank
    private String description;

}
