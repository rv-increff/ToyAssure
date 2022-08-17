package assure.pojo;

import assure.util.ConsumerTypes;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import static assure.pojo.TableConstants.*;


@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "type"})})
public class ConsumerPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = SEQ_CLIENT, initialValue = SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ_CLIENT)
    private Long id;

    @Column(nullable = false)
    private String name;

    private ConsumerTypes type; //TODO change to party types


}
