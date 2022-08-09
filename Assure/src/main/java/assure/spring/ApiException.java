package assure.spring;

import assure.model.ErrorForm;

import java.util.List;

public class ApiException extends Exception {
    private static final long serialVersionUID = 1L;

    public ApiException(String string) {
        super(string);
    }

    public ApiException(List<ErrorForm> errorFormList){
        String err = "";
        for(ErrorForm errorForm : errorFormList){
            err += errorForm.toString();
        }
        new ApiException(err);
    }


}
