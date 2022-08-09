package assure.model;

import assure.util.Types;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientData {

    private Long id;
    private String name;

    private Types type;
}
