package assure.dto;

import assure.pojo.ChannelPojo;
import assure.pojo.PartyPojo;
import commons.model.ChannelListingData;
import assure.model.ChannelListingForm;
import assure.model.ChannelListingUploadForm;
import assure.pojo.ChannelListingPojo;
import assure.service.ChannelListingService;
import assure.service.ChannelService;
import assure.service.PartyService;
import assure.service.ProductService;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static assure.util.ConversionUtil.*;
import static assure.util.ValidationUtil.*;
import static java.util.Objects.isNull;

@Service
public class ChannelListingDto {
    private static final Long MAX_LIST_SIZE = 1000L;
    private static final Integer PAGE_SIZE = 5;
    @Autowired
    private ChannelListingService channelListingService;
    @Autowired
    private PartyService partyService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ChannelService channelService;

    public Integer add(ChannelListingUploadForm channelListingUploadForm) throws ApiException {

        validateForm(channelListingUploadForm);
        validateList("Channel Listing Upload",channelListingUploadForm.getChannelListingFormList(), MAX_LIST_SIZE);
        checkDuplicateChannelListingFormList(channelListingUploadForm.getChannelListingFormList());

        Long clientId = channelListingUploadForm.getClientId();
        Long channelId = channelListingUploadForm.getChannelId();
        partyService.getCheck(clientId);
        channelService.getCheck(channelId);

        channelListingService.add(convertChannelListingFormToPojo(clientId, channelId,
                channelListingUploadForm.getChannelListingFormList()));

        return channelListingUploadForm.getChannelListingFormList().size();
    }

    public List<ChannelListingData> select(Integer pageNumber) throws ApiException {
        return convertChannelListingPojoListToData(channelListingService.select(pageNumber,PAGE_SIZE));
    }

    public List<ChannelListingData> selectByChannelIdAndClientId(Long channelId, Long clientId) throws ApiException {
        if(isNull(channelId) || isNull(clientId))
            throw new ApiException("channel Id or client Id are NULL");

        return convertChannelListingPojoListToData(channelListingService.selectByChannelIdAndClientId(channelId, clientId));
    }

    private List<ChannelListingPojo> convertChannelListingFormToPojo(Long clientId, Long channelId,
                                                                     List<ChannelListingForm> channelListingFormList)
            throws ApiException {

        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();

        List<String> clientSkuIdList = channelListingFormList.stream().map(ChannelListingForm::getClientSkuId).distinct()
                .collect(Collectors.toList());
        Map<String, Long> clientSkuIdToGsku = productService.getCheckClientSkuId(
                clientSkuIdList, clientId);

        for (ChannelListingForm channelListingForm : channelListingFormList) {

            ChannelListingPojo channelListingPojo = new ChannelListingPojo();
            channelListingPojo.setGlobalSkuId(clientSkuIdToGsku.get(channelListingForm.getClientSkuId()));
            channelListingPojo.setChannelId(channelId);
            channelListingPojo.setClientId(clientId);
            channelListingPojo.setChannelSkuId(channelListingForm.getChannelSkuId());

            channelListingPojoList.add(channelListingPojo);
        }

        return channelListingPojoList;
    }

    private List<ChannelListingData> convertChannelListingPojoListToData(List<ChannelListingPojo> channelListingPojoList) throws ApiException {
        if (CollectionUtils.isEmpty(channelListingPojoList))
            return new ArrayList<>();

        Map<Long, ChannelPojo> channelIdToPojo = channelService.getCheckChannelIdToPojo(channelListingPojoList.stream().
                map(ChannelListingPojo::getChannelId).distinct().collect(Collectors.toList()));
        Map<Long, PartyPojo> partyIdToPojo = partyService.getCheckPartyIdToPojo(channelListingPojoList.stream().
                map(ChannelListingPojo::getClientId).distinct().collect(Collectors.toList()));

        List<ChannelListingData> channelListingDataList = new ArrayList<>();
        for (ChannelListingPojo channelListingPojo : channelListingPojoList) {

            channelListingDataList.add(convertChannelListingPojoToData(channelListingPojo,
                    channelIdToPojo.get(channelListingPojo.getChannelId()).getName(),
                    partyIdToPojo.get(channelListingPojo.getClientId()).getName()));
        }
        return channelListingDataList;
    }

}
