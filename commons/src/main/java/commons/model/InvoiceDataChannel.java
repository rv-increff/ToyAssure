package commons.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@XmlRootElement
public class InvoiceDataChannel {
    private String invoiceGenerationTime;
    private String channelOrderId;
    private List<OrderItemChannelData> orderItemChannelDataList;
    private Double total;

    public InvoiceDataChannel(){}
    public InvoiceDataChannel(ZonedDateTime invoiceGenerationTime, String channelOrderId, List<OrderItemChannelData> orderItemChannelDataList, Double total){
        this.invoiceGenerationTime = invoiceGenerationTime.toLocalDate().toString() + " " + invoiceGenerationTime.toLocalTime().toString();
        this.channelOrderId = channelOrderId;
        this.orderItemChannelDataList = orderItemChannelDataList;
        this.total = total;
    }
}
