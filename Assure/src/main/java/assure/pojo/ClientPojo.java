package assure.pojo;

import assure.util.Types;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static assure.pojo.TableConstants.*;

@Getter
@Setter
@Entity
@Table(name = "assure_client")
public class ClientPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = ASSURE_GENERATOR, initialValue = CLIENT_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = ASSURE_GENERATOR)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Types types;


}
