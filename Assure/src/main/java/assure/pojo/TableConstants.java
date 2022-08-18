package assure.pojo;

public class TableConstants {
    public final static String SEQ_PRODUCT = "seq_product";
    public final static String SEQ_PARTY = "seq_client";
    public final static String SEQ_BIN_SKU = "seq_bin_sku";
    public final static String SEQ_BIN = "seq_bin";
    public final static String SEQ_CHANNEL = "seq_channel";
    public final static String SEQ_CHANNEL_LISTING = "seq_channel_listing";
    public final static String INDEX_BIN_ID_GLOBAL_SKU_ID = "index_bin_id_global_sku_id";
    public final static String INDEX_NAME_TYPE = "index_name_type"; //TODO only if it gets big
    public final static String INDEX_CLIENT_SKU_ID_CLIENT_ID = "index_client_sku_id_client_id";
    //TODO where order in increasing number of occurence client id then suid
    public final static String INDEX_CLIENT_ID = "index_client_id";
    public final static String INDEX_CLIENT_SKU_ID = "index_client_sku_id";
    public final static String INDEX_CHANNEL_LISTING = "index_channel_listing";
    public final static int SEQ_INITIAL_VALUE = 100000;


}
