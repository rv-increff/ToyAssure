package assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static assure.pojo.TableConstants.*;

@Getter
@Setter
@Entity
@Table(indexes = {@Index(name = INDEX_CLIENT_SKU_ID_CLIENT_ID, columnList = "clientSkuId, clientId"), //TODO check naming
                    @Index(name=INDEX_CLIENT_ID, columnList = "clientId"),
                    @Index(name=INDEX_CLIENT_SKU_ID, columnList = "clientSkuId")
},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"clientSkuId", "clientId"})})
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
