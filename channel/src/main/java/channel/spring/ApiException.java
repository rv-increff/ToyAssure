package channel.spring;

import commons.model.ErrorData;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
@Getter

public class ApiException extends Exception {
    private static final long serialVersionUID = 1L;
    private List<ErrorData> errorFormList;
    private Integer errorType = 0;

    public ApiException(String string) {
        super(string);
    }

    public ApiException(List<ErrorData> errorFormList) {
        this.errorFormList = errorFormList;
        this.errorType = 1;
    }

}
