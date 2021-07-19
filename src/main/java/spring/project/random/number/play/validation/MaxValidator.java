package spring.project.random.number.play.validation;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import spring.project.random.number.play.utils.CalcUtils;

public class MaxValidator implements ConstraintValidator<Max, Number> {

    BigDecimal max;
    String message;
    String annotationValue;
    private boolean inclusive;

    @Override
    public void initialize(
            Max annotation) {
        annotationValue = annotation.value();
        max = ValidationHelper.propertyValueAsBigDecimal(annotationValue);
        message = annotation.message();
        inclusive = annotation.exclusive();
    }

    @Override
    public boolean isValid(
            Number value,
            ConstraintValidatorContext context) {
        if (value == null) {
            return true;// other validator must check
        }

        if (CalcUtils.isFirstBigger(value, max)) {
            ValidationHelper.reportConstraintViolation(context, message, "value", max.toString());
            return false;
        }
        return true;
    }
}
