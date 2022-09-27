package channel.controller;

public class RestPath {
    public static final String ASSURE_CHANNEL_ORDER_URL = "orders/channel-orders";
    public static final String ASSURE_SELECT_ORDER_URL = "orders?invoiceType=CHANNEL&pageNumber=";
    public static final String ASSURE_SELECT_ORDER_ITEM_URL = "orders/%d/order-items";
    public static final String ASSURE_PARTY_SEARCH_URL = "parties/search";
    public static final String ASSURE_SELECT_CHANNEL_URL = "channels";
    public static final String ASSURE_SELECT_CHANNEL_LISTING_URL = "channel-listings?channelId=%d&clientId=%d";
}
