package assure.util;

import commons.model.ErrorData;
import assure.model.ProductForm;
import assure.pojo.BinSkuPojo;
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
        if(isNull(productFormList))
            throw new ApiException("Product Form list is null");

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

    public static void checkDuplicateGlobalSkuAndBinIdPair(List<BinSkuPojo> binSkuPojoList) throws ApiException {
        if(isNull(binSkuPojoList))
            throw new ApiException("Bin Sku Pojo list is null");

        HashMap<Long, Set<Long>> clientSkuIdToBinId = new HashMap<>(); //TODO change logic to string->set of bin ids
       //TODO use get or default
        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
                if (clientSkuIdToBinId.getOrDefault(binSkuPojo.getGlobalSkuId(), new HashSet<>()).contains(binSkuPojo.getBinId()))
                    throw new ApiException( "duplicate values of globalSkuId-binId pair");
                else {
                    if (isNull(clientSkuIdToBinId.get(binSkuPojo.getGlobalSkuId()))) {
                        clientSkuIdToBinId.put(binSkuPojo.getGlobalSkuId(), new HashSet<>());
                    }
                    clientSkuIdToBinId.getOrDefault(binSkuPojo.getGlobalSkuId(), new HashSet<>()).add(binSkuPojo.getBinId());
                }
        }

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

    public static void throwErrorIfNotEmpty(List<ErrorData> errorFormList) throws ApiException {
        if (!CollectionUtils.isEmpty(errorFormList)) {
            throw new ApiException(errorFormList);
        }
    }
    public static <T> void validateForm(T form) throws ApiException {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(form);
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            throw new ApiException( constraintViolation.getPropertyPath().toString()
                    + " " + constraintViolation.getMessage());
        }
    }

    public static <T> void validateFormList(List<T> formList) throws ApiException{
        for (T form : formList) {
            validateForm(form);
        }
    }
}
