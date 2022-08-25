package assure.model;

import assure.util.InvoiceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelData {

    private String name;
    private InvoiceType invoiceTypes;
}
