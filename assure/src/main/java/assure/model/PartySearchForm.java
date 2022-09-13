package assure.model;

import assure.util.PartyType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartySearchForm {
    private PartyType type;
    private Integer pageNumber;
}
