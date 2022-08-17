package assure.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class BinSkuUpdateForm {
    @Min(value = 0)
    private Long quantity;
}
