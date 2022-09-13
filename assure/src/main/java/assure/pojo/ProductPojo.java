package assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static assure.pojo.TableConstants.*;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"clientSkuId", "clientId"}, name = UK_PRODUCT)})
//TODO read and add uk name
public class ProductPojo extends AbstractPojo {

    @Id
    @TableGenerator(name = SEQ_PRODUCT, initialValue = SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ_PRODUCT)
    private Long globalSkuId;

    @Column(nullable = false)
    private String clientSkuId;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brandId;

    @Column(nullable = false)
    private Double mrp;

    @Column(nullable = false)
    private String description;

}
