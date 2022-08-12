package assure.util;


import assure.spring.ApiException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class DataUtil {
    public static <T> boolean validateNullCheck(T form){
        try {

            Field[] fields = form.getClass().getDeclaredFields();
            for (Field m : fields) {
                m.setAccessible(true);
                System.out.println(m.get(form) + "-" + m.getName());
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
    public static <T> List<List<T>> partition(List<T> list,Integer parts){
        Integer size = list.size();
        List<List<T>> subLists = new ArrayList<>();

        Integer row = 1;
        while(row<=parts){
            subLists.add(list.subList((row-1)*size/parts, (row)*size/parts));
            row++;
        }
        return subLists;
    }
}
