package assure.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinSkuData {
    private Long id;
    private Long binId;
    private Long quantity;
    private Long globalSkuId;
}
