package assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static assure.pojo.TableConstants.SEQ_BIN_SKU;
import static assure.pojo.TableConstants.SEQ_INITIAL_VALUE;

@Getter
@Setter
@Entity
public class BinSkuPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = SEQ_BIN_SKU, initialValue = SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ_BIN_SKU)
    private Long id;

    @Column(nullable = false)
    private Long binId;

    @Column(nullable = false)
    private Long globalSkuId;

    @Column(nullable = false)
    private Long quantity;
}
