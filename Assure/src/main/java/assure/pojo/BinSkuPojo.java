package assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static assure.pojo.TableConstants.ASSURE_GENERATOR;
import static assure.pojo.TableConstants.CLIENT_INITIAL_VALUE;

@Getter
@Setter
@Entity
@Table(name = "assure_bin_sku")
public class BinSkuPojo {
    @Id
    @TableGenerator(name = ASSURE_GENERATOR, initialValue = CLIENT_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = ASSURE_GENERATOR)
    private Long id;

    @Column(nullable = false)
    private Long binId;

    @Column(nullable = false)
    private Long globalSkuId;

    @Column(nullable = false)
    private Long quantity;
}
