package assure.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
@Getter
@Setter
public class ChannelListingData {
    private Long id;
    private Long channelId;
    private String channelSkuId;
    private Long clientId;
    private Long globalSkuId;
}
