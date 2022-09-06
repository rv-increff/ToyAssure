package assure.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class BinSkuUpdateForm {
    @PositiveOrZero //TODO remove
    private Long quantity;
}
