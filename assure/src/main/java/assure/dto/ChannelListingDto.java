package assure.dto;

import assure.model.ChannelListingForm;
import assure.model.ChannelListingUploadForm;
import commons.model.ErrorData;
import assure.pojo.ChannelListingPojo;
import assure.pojo.ProductPojo;
import assure.service.ChannelListingService;
import assure.service.ChannelService;
import assure.service.PartyService;
import assure.service.ProductService;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static assure.util.ConversionUtil.checkDuplicateChannelListingFormList;
import static assure.util.ValidationUtil.*;
import static java.util.Objects.isNull;

@Service
public class ChannelListingDto {
    private static final Long MAX_LIST_SIZE = 1000L;
    @Autowired
    private ChannelListingService channelListingService;
    @Autowired
    private PartyService partyService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ChannelService channelService;

    @Transactional(rollbackFor = ApiException.class)//TODO only if more than one update/add
    public Integer add(ChannelListingUploadForm channelListingUploadForm) throws ApiException {

        validateForm(channelListingUploadForm);   //TODO club all three fns
        validateListSize(channelListingUploadForm.getChannelListingFormList(), MAX_LIST_SIZE);
        checkDuplicateChannelListingFormList(channelListingUploadForm.getChannelListingFormList());

        Long clientId = channelListingUploadForm.getClientId();
        Long channelId = channelListingUploadForm.getChannelId();
        partyService.getCheck(clientId);
        channelService.getCheck(channelId);

        channelListingService.add(transformAndConvertChannelListingFormToPojo(clientId, channelId,
                channelListingUploadForm.getChannelListingFormList()));

        return channelListingUploadForm.getChannelListingFormList().size();
    }


    private List<ChannelListingPojo> transformAndConvertChannelListingFormToPojo(Long clientId, Long channelId,
                                                                                 List<ChannelListingForm> channelListingFormList)
            throws ApiException {

        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();

        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (ChannelListingForm channelListingForm : channelListingFormList) {
            ChannelListingPojo channelListingPojo = new ChannelListingPojo();
//TODO use map to get data getCheckMap
            ProductPojo productPojo = productService.selectByClientSkuIdAndClientId(channelListingForm.getClientSkuId(),
                    clientId);
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
