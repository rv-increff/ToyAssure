package assure.model;

import assure.util.Types;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ClientForm {

    @NonNull
    private String name;
    @NonNull
    private Types types;
}
