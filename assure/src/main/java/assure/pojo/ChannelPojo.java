package assure.pojo;

import assure.util.InvoiceType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static assure.pojo.TableConstants.*;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = UK_CHANNEL)})
public class ChannelPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = SEQ_CHANNEL, initialValue = SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ_CHANNEL)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private InvoiceType invoiceType;

}
