package assure.util;

import assure.pojo.ChannelPojo;
import assure.pojo.ProductPojo;

public class NormalizeUtil {
    public static void normalizeChannelPojo(ChannelPojo channelPojo) {
        channelPojo.setName(channelPojo.getName().toUpperCase());
    }
    public static void normalizeProductPojo(ProductPojo productPojo){
        productPojo.setClientSkuId(productPojo.getClientSkuId().toLowerCase());
    }
}
