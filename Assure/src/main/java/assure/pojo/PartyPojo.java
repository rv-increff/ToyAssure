package assure.pojo;

import assure.util.PartyTypes;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import static assure.pojo.TableConstants.*;


@Getter
@Setter
@Entity
@Table(indexes = {@Index(name = INDEX_NAME_TYPE ,columnList = "name, type")},uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "type"})})
public class PartyPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = SEQ_PARTY, initialValue = SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ_PARTY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private PartyTypes type; //TODO change to party types


}
