package assure.util;


import assure.spring.ApiException;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class DataUtil {
    public static <T> boolean validate(T form){
        try {

            Field[] fields = form.getClass().getDeclaredFields();
            for (Field m : fields) {
                m.setAccessible(true);
                System.out.println(m.get(form));
                if(isNull(m.get(form)))return false;
            }
        } catch (IllegalAccessException err) {
            System.out.println(err);
        }
        return true;
    }

    public static Boolean validateMRP(Double MRP) throws ApiException {
        Pattern numP = Pattern.compile("^[0-9]+$|^[0-9]+\\.[0-9]*$");
        Matcher matcher = numP.matcher(MRP.toString());
        return matcher.find();
    }
}
