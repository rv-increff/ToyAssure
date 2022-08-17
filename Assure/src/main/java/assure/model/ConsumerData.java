package assure.model;

import assure.util.ConsumerTypes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerData {

    private Long id;
    private String name;
    private ConsumerTypes type;
}
