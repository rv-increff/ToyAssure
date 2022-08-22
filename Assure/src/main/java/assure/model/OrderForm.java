package assure.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class OrderForm {
    @Min(value = 0)
    Long clientId;

    @NotBlank
    String channelOrderId;

    @Min(value = 0)
    Long customerId;

    @Min(value = 0)
    Long channelId;

    @NotEmpty
    List<@NotNull OrderItemForm> orderItemFormList;
}
