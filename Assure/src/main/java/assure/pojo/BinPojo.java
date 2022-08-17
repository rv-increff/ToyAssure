package assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static assure.pojo.TableConstants.SEQ_BIN;
import static assure.pojo.TableConstants.SEQ_INITIAL_VALUE;

@Getter
@Setter
@Entity
public class BinPojo extends AbstractPojo {

    @Id
    @TableGenerator(name = SEQ_BIN, initialValue = SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ_BIN)
    private Long binId;

}
