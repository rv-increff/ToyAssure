package assure.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ChannelListingUploadForm {
    @NotNull
    Long clientId;

    @NotNull
    Long channelId;

    @NotEmpty
    List<@NotNull ChannelListingForm> channelListingFormList;
}
