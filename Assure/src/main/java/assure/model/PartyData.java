package assure.model;

import assure.util.PartyTypes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyData {

    private Long id;
    private String name;
    private PartyTypes type;
}
