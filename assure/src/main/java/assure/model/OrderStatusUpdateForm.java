package assure.model;

import commons.util.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderStatusUpdateForm {
    @NotNull
    private Long orderId;
    @NotNull
    private OrderStatus updateStatusTo;
}
