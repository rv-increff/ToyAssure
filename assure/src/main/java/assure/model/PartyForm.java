package assure.model;

import assure.util.PartyType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PartyForm {
    @NotBlank
    private String name;
    @NotNull
    private PartyType type;
}
