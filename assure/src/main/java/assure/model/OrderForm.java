package assure.model;

import lombok.Getter;
import lombok.Setter;
import commons.model.OrderItemForm;

import javax.validation.constraints.*;
import java.util.List;

@Setter
@Getter
public class OrderForm {
    @PositiveOrZero
    Long clientId;

    @NotBlank
    String channelOrderId;

    @PositiveOrZero
    Long customerId;

    @NotEmpty
    List<@NotNull OrderItemForm> orderItemFormList;
}
