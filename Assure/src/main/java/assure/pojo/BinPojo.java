package assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static assure.pojo.TableConstants.*;

@Getter
@Setter
@Entity
@Table(name = "assure_bin")
public class BinPojo {

    @Id
    @TableGenerator(name = ASSURE_GENERATOR, initialValue = BIN_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = ASSURE_GENERATOR)
    private Long globalSkuId;
}
