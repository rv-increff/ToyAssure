package assure.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChannelListingForm {
    @NotBlank
    private String channelSkuId;
    @NotBlank
    private String clientSkuId;
}
