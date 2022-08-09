package assure.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


@Getter
@Setter
public class ProductForm {

    @NonNull
    private String clientSkuId;

    @NonNull
    private String name;

    @NonNull
    private String brandId;

    @NonNull
    private Double mrp;

    @NonNull
    private String description;

}
