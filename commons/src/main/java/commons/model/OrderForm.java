package commons.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
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

    @PositiveOrZero
    Long channelId;

    @NotEmpty
    List<OrderItemForm> orderItemFormList;
}
