package assure.model;

import assure.util.PartyType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyData {

    private Long id;
    private String name;
    private PartyType type;
}
