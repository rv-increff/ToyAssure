package assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static assure.pojo.TableConstants.*;

@Getter
@Setter
@Entity
@Table(name = "assure_bin")
public class BinPojo extends AbstractPojo{

    @Id
    @TableGenerator(name = BIN_GENERATOR, initialValue = INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = BIN_GENERATOR)
    private Long binId;

}
