package assure.util;

import assure.pojo.ChannelListingPojo;
import assure.pojo.ChannelPojo;
import assure.pojo.PartyPojo;
import assure.pojo.ProductPojo;
import assure.spring.ApiException;

import static java.util.Objects.isNull;

public class NormalizeUtil {
    public static void normalizeChannelPojo(ChannelPojo channelPojo) throws ApiException {
        if(isNull(channelPojo))
            throw new ApiException("Channel Pojo is null");
        channelPojo.setName(channelPojo.getName().toUpperCase().trim());
    }
    public static String normalizeString(String name) throws ApiException {
        if(isNull(name))
            throw new ApiException("String name is null");
        return name.toUpperCase().trim();
    }
    public static void normalizeChannelListingPojo(ChannelListingPojo channelListingPojo) {
        channelListingPojo.setChannelSkuId(channelListingPojo.getChannelSkuId().toLowerCase().trim());
    }
    public static void normalizePartyPojo(PartyPojo partyPojo) {
        partyPojo.setName(partyPojo.getName().toLowerCase().trim());
    }
    public static void normalizeProductPojo(ProductPojo productPojo){
        productPojo.setClientSkuId(productPojo.getClientSkuId().toLowerCase().trim());
    }

}
