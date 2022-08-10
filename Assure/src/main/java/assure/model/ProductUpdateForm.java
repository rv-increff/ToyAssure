package assure.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateForm {
    private String clientSkuId;
    private String name;
    private String brandId;
    private Double mrp;
    private String description;
}
