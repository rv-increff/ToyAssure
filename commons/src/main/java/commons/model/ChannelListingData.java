package commons.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelListingData {
    private Long id;
    private String channelName;
    private String channelSkuId;
    private String clientName;
    private Long globalSkuId;
}
