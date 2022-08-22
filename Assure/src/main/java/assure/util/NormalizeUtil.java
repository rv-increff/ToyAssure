package assure.util;

import assure.pojo.ChannelPojo;

public class NormalizeUtil {
    public static void normalizeChannelPojo(ChannelPojo channelPojo) {
        channelPojo.setName(channelPojo.getName().toUpperCase());
    }
}
