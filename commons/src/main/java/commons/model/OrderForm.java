package commons.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Setter
@Getter
public class OrderForm {
    @NotNull
    Long clientId;

    @NotBlank
    String channelOrderId;

    @NotNull
    Long customerId;

    @NotEmpty
    List< @NotNull OrderItemForm> orderItemFormList;
}
