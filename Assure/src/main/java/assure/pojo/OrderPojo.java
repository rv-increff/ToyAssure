package assure.pojo;

import assure.util.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static assure.pojo.TableConstants.SEQ_INITIAL_VALUE;
import static assure.pojo.TableConstants.SEQ_ORDER;

@Getter
@Setter
@Entity
public class OrderPojo extends AbstractPojo {

    @Id
    @TableGenerator(name = SEQ_ORDER, initialValue = SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ_ORDER)
    private Long id;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Long channelId;

    @Column(nullable = false)
    private String channelOrderId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

}
