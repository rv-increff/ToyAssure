package assure.dto;

import assure.model.ChannelData;
import assure.model.ChannelForm;
import assure.service.ChannelService;
import assure.spring.ApiException;
import assure.util.InvoiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static assure.util.Helper.*;

@Service
public class ChannelDto {

    private static final Integer PAGE_SIZE = 10;
    @Autowired
    private ChannelService channelService;


    public List<ChannelData> select(Integer pageNumber) {
        return convertChannelPojoListToData(channelService.select(pageNumber, PAGE_SIZE));
    }

    public ChannelForm add(ChannelForm channelForm) throws ApiException {
        validate(channelForm);
        if(channelForm.getInvoiceType() == InvoiceType.SELF){
            throw new ApiException("Invoice type cannot be self"); //TODO check correct;
        }
        channelService.add(convertChannelFormToPojo(channelForm));
        return channelForm;
    }


}
