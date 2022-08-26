package assure.util;

import assure.pojo.ChannelListingPojo;
import assure.pojo.ChannelPojo;
import assure.pojo.PartyPojo;
import assure.pojo.ProductPojo;

public class NormalizeUtil {
    public static void normalizeChannelPojo(ChannelPojo channelPojo) {
        channelPojo.setName(channelPojo.getName().toUpperCase());
    }
    public static void normalizeChannelListingPojo(ChannelListingPojo channelListingPojo) {
        channelListingPojo.setChannelSkuId(channelListingPojo.getChannelSkuId().toUpperCase());
    }
    public static void normalizePartyPojo(PartyPojo partyPojo) {
        partyPojo.setName(partyPojo.getName().toLowerCase());
    }
    public static void normalizeProductPojo(ProductPojo productPojo){
        productPojo.setClientSkuId(productPojo.getClientSkuId().toLowerCase());
    }

}