package assure.dto;

import assure.model.ChannelListingForm;
import assure.model.ErrorData;
import assure.pojo.ChannelListingPojo;
import assure.pojo.ChannelPojo;
import assure.pojo.PartyPojo;
import assure.pojo.ProductPojo;
import assure.service.ChannelListingService;
import assure.service.ChannelService;
import assure.service.PartyService;
import assure.service.ProductService;
import assure.spring.ApiException;
import assure.util.PartyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static assure.util.Helper.*;
import static java.util.Objects.isNull;

@Service
public class ChannelListingDto {

    @Autowired
    private ChannelListingService channelListingService;
    @Autowired
    private PartyService partyService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ChannelService channelService;

    @Transactional(rollbackFor = ApiException.class)
    public Integer add(String clientName, String channelName, List<ChannelListingForm> channelListingFormList)
            throws ApiException {

        validateList("Channel Listing", channelListingFormList);
        checkDuplicateChannelListingFormList(channelListingFormList);


        PartyPojo partyPojo = partyService.selectByNameAndPartyType(clientName, PartyType.CLIENT);
        if (isNull(partyPojo)) {
            throw new ApiException("client name does not exist");
        }
        ChannelPojo channelPojo = channelService.selectByName(channelName);
        if (isNull(channelPojo)) {
            throw new ApiException("channel name does not exist");
        }

        channelListingService.add(transformAndConvertChannelListingFormToPojo(partyPojo.getId(), channelPojo.getId(), channelListingFormList));
        return channelListingFormList.size();
    }



    private List<ChannelListingPojo> transformAndConvertChannelListingFormToPojo(Long clientId, Long channelId,
                                                                                 List<ChannelListingForm> channelListingFormList)
            throws ApiException {

        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();

        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (ChannelListingForm channelListingForm : channelListingFormList) {
            ChannelListingPojo channelListingPojo = new ChannelListingPojo();

            ProductPojo productPojo = productService.selectByClientSkuId(channelListingForm.getClientSkuId());
            if (isNull(productPojo)) {
                errorFormList.add(new ErrorData(row, "client skuId does not exists"));
                continue;
            }
            channelListingPojo.setGlobalSkuId(productPojo.getGlobalSkuId());
            channelListingPojo.setChannelId(channelId);
            channelListingPojo.setClientId(clientId);
            channelListingPojo.setChannelSkuId(channelListingForm.getChannelSkuId());
            channelListingPojoList.add(channelListingPojo);
            row++;
        }

        throwErrorIfNotEmpty(errorFormList);
        return channelListingPojoList;
    }
}
