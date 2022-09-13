package assure.model;

import lombok.Getter;
import lombok.Setter;
import commons.model.OrderItemForm;

import javax.validation.constraints.*;
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
    List<@NotNull OrderItemForm> orderItemFormList;
}
