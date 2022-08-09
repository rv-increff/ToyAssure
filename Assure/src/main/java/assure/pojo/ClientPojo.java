package assure.pojo;

import assure.util.Types;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import static assure.pojo.TableConstants.*;


@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "type"})}, name = "assure_client")
public class ClientPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = CLIENT_GENERATOR, initialValue = INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = CLIENT_GENERATOR)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Types type;


}
