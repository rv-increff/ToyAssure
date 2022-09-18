package commons.model;

import commons.util.InvoiceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelData {
    private Long id;
    private String name;
    private InvoiceType invoiceTypes;
}
