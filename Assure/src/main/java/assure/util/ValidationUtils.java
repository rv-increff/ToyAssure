package assure.util;


import assure.spring.ApiException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class ValidationUtils {
    public static <T> boolean checkNotNull(T form){ //TODO use annotation and its validations
        try {
            Field[] fields = form.getClass().getDeclaredFields();
            for (Field m : fields) {
                m.setAccessible(true);
                if(isNull(m.get(form)))return false;
            }
        } catch (IllegalAccessException err) {
        }
        return true;
    }
}
