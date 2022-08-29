package assure.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@XmlRootElement
public class InvoiceData {
    private String invoiceGenerationTime;
    private String channelOrderId;
    private List<OrderItemInvoiceData> orderItemInvoiceDataList;
    private Double total;

    public InvoiceData(){}
    public InvoiceData(ZonedDateTime invoiceGenerationTime, String channelOrderId, List<OrderItemInvoiceData> orderItemInvoiceDataList, Double total){
        this.invoiceGenerationTime = invoiceGenerationTime.toLocalDate().toString() + " " + invoiceGenerationTime.toLocalTime().toString();
        this.channelOrderId = channelOrderId;
        this.orderItemInvoiceDataList = orderItemInvoiceDataList;
        this.total = total;
    }
}
