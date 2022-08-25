package assure.model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class BinSkuForm {
    @NotNull
    private Long clientId;
    @NotEmpty
    private List<BinSkuItemForm> binSkuItemFormList;
}
