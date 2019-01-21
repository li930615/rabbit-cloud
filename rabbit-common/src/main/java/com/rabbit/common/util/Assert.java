package com.rabbit.common.util;

import com.rabbit.common.util.exception.CheckedException;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @ClassName Assert
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/19 19:44
 **/
public class Assert {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static void validateEntity(Object object, Class<?>[] groups)
            throws CheckedException
    {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for (ConstraintViolation constraint : constraintViolations) {
                msg.append(constraint.getMessage()).append("<br>");
            }
            throw new CheckedException(msg.toString());
        }
    }

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str))
            throw new CheckedException(message);
    }

    public static void isNull(Object object, String message)
    {
        if (object == null)
            throw new CheckedException(message);
    }
}
