package assure.util;

import assure.model.BinSkuForm;
import assure.model.ErrorData;
import assure.model.ProductForm;
import assure.spring.ApiException;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.*;

import static java.util.Objects.isNull;

public class ValidationUtil {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();


    public static void checkDuplicateProductsProductForm(List<ProductForm> productFormList) throws ApiException {

        HashSet<String> set = new HashSet<>();
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (ProductForm productForm : productFormList) {
            if (set.contains(productForm.getClientSkuId())) {
                errorFormList.add(new ErrorData(row, "duplicate values of clientSkuId"));
            }
            set.add(productForm.getClientSkuId());
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }

    public static void checkDuplicateBinSkuAndBinIdPairBinSkuForm(List<BinSkuForm> binSkuFormList) throws ApiException {

        HashMap<String,Long> clientSkuIdToBinIdMap = new HashMap<>();
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;

        for (BinSkuForm binSkuForm : binSkuFormList) {
            if(clientSkuIdToBinIdMap.containsKey(binSkuForm.getClientSkuId())) {
                if (clientSkuIdToBinIdMap.get(binSkuForm.getClientSkuId()) == binSkuForm.getBinId()) {
                    errorFormList.add(new ErrorData(row, "duplicate values of clientSkuId-binId pair"));
                }
            }else
                clientSkuIdToBinIdMap.put(binSkuForm.getClientSkuId(), binSkuForm.getBinId());

            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }

    public static <T> void validateList(String name, List<T> formList, Long maxListSize) throws ApiException {
        if (CollectionUtils.isEmpty(formList)) {
            throw new ApiException(name + " List cannot be empty");
        }
        if (formList.size() > maxListSize) {
            throw new ApiException(name + " List size more than limit, limit : " + maxListSize);
        }
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        for (T form : formList) {
            Set<ConstraintViolation<T>> constraintViolations =
                    validator.validate(form);

            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                errorFormList.add(new ErrorData(row, constraintViolation.getPropertyPath().toString() + " " + constraintViolation.getMessage()));
            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }

    public static <T> void validateListSize(List<T> formList, Long maxListSize) throws ApiException {
        if (CollectionUtils.isEmpty(formList)) {
            throw new ApiException("Form List cannot be empty");
        }

        if (formList.size() > maxListSize) {
            throw new ApiException("list size more than max limit, limit : " + maxListSize);
        }


    }

    public static void throwErrorIfNotEmpty(List<ErrorData> errorFormList) throws ApiException {
        if (!CollectionUtils.isEmpty(errorFormList)) {
            throw new ApiException(errorFormList);
        }
    }
    public static <T> void validate(T form) throws ApiException {
        List<ErrorData> errorFormList = new ArrayList<>();
        Integer row = 1;
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(form);
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            errorFormList.add(new ErrorData(row, constraintViolation.getPropertyPath().toString()
                    + " " + constraintViolation.getMessage()));
        }
        row++;
        throwErrorIfNotEmpty(errorFormList);
    }
    public static <T> void validateAddPojo(T pojo, List<String> excludeList) throws ApiException {
        if (isNull(pojo)) {
            throw new ApiException(" Pojo object can not be null");
        }
        try {
            Field[] fields = pojo.getClass().getDeclaredFields();
            for (Field m : fields) {
                m.setAccessible(true);
                if (isNull(m.get(pojo)) && !excludeList.contains(m.getName())) {
                    throw new ApiException(m.getName() + " cannot be null in Pojo object");
                }
            }
        } catch (IllegalAccessException err) {
            System.out.println(err);
        }
    }

    public static <T> void validateAddPojoList(List<T> pojoList, List<String> excludeList, Long maxListSize) throws ApiException {
        if (CollectionUtils.isEmpty(pojoList)) {
            throw new ApiException("List cannot be empty or null");
        }

        if (pojoList.size() > maxListSize) {
            throw new ApiException("list size more than max limit, limit : " + maxListSize);
        }

        for (T pojo : pojoList) {
            validateAddPojo(pojo, excludeList);
        }
    }

}
