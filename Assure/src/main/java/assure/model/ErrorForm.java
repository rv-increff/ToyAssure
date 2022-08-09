package assure.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorForm {

    private Integer row;
    private String message;

    public ErrorForm(Integer row, String message){
        this.row = row;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Error row " + this.row + " " + this.message + "\n";
    }
}
