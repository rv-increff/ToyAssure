package assure.util;

import assure.model.OrderStatusUpdateForm;
import assure.pojo.BinSkuPojo;

import java.util.Map;

public class DataUtil {
    public static int binSkuPojoSortByQtyComparator(BinSkuPojo binSkuPojo1, BinSkuPojo binSkuPojo2){
        return (int)( binSkuPojo1.getQuantity() - binSkuPojo2.getQuantity());
    }

}

