package assure.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
public class ProductForm {
    @NotBlank
    private String clientSkuId;
    @NotBlank
    private String name;
    @NotBlank
    private String brandId;
    @Min(value = 0)
    private Double mrp;
    @NotBlank
    private String description;

}
