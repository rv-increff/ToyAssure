package assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static assure.pojo.TableConstants.*;

@Getter
@Setter
@Entity
@Table(name = "assure_bin_sku")
public class BinSkuPojo {
    @Id
    @TableGenerator(name = BIN_SKU_GENERATOR, initialValue = INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = BIN_SKU_GENERATOR)
    private Long id;

    @Column(nullable = false)
    private Long binId;

    @Column(nullable = false)
    private Long globalSkuId;

    @Column(nullable = false)
    private Long quantity;
}
