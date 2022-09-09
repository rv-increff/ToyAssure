package assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static assure.pojo.TableConstants.*;

@Getter
@Setter
@Entity
//@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"OrderId","globalSkuId"})})
public class OrderItemPojo extends AbstractPojo{

    @Id
    @TableGenerator(name = SEQ_ORDER_ITEM, initialValue = SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ_ORDER_ITEM)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long globalSkuId;

    @Column(nullable = false)
    private Long orderedQuantity;

    @Column(nullable = false)
    private Long allocatedQuantity;

    @Column(nullable = false)
    private Long fulfilledQuantity;

    @Column(nullable = false)
    private Double sellingPricePerUnit;


}
