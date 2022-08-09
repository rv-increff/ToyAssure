package assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static assure.pojo.TableConstants.*;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"clientSkuId", "clientId"})}, name = "assure_product")
public class ProductPojo {
    @Id
    @TableGenerator(name = PRODUCT_GENERATOR, initialValue = INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = PRODUCT_GENERATOR)
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
