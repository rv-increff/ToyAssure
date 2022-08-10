package assure.spring;

import assure.model.ErrorForm;
import lombok.Getter;

import java.util.List;

import static assure.util.Helper.transformErrorList;
@Getter
public class ApiException extends Exception {
    private static final long serialVersionUID = 1L;
    private List<ErrorForm> errorFormList;
    private Integer errorType = 0;
    public ApiException(String string) {
        super(string);
    }

    public ApiException(List<ErrorForm> errorFormList){
        this.errorFormList = errorFormList;
        this.errorType=1;
    }
}
