package spring.project.random.number.play.validation;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import spring.project.random.number.play.utils.CalcUtils;

public class MinValidator implements ConstraintValidator<Min, Number> {

    BigDecimal min;
    String message;
    String annotationValue;

    @Override
    public void initialize(
            Min annotation) {
        annotationValue = annotation.value();
        min = ValidationHelper.propertyValueAsBigDecimal(annotationValue);
        message = annotation.message();
    }

    @Override
    public boolean isValid(
            Number value,
            ConstraintValidatorContext context) {
        if (value == null) {
            return true;// other validator must check
        }

        if (CalcUtils.isFirstLess(value, min)) {
            ValidationHelper.reportConstraintViolation(context, message, "value", min.toString());
            return false;
        }
        return true;
    }
}
