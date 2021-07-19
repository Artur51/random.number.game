package spring.project.random.number.play.validation;

import java.math.BigDecimal;
import java.util.Locale;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;

import spring.project.random.number.play.config.AppConfiguration;

public class ValidationHelper {
    /**
     * report ConstraintValidator error into given context.
     */
    public static ConstraintValidatorContext reportConstraintViolation(
            ConstraintValidatorContext context,
            String message) {
        context.disableDefaultConstraintViolation();
        ConstraintViolationBuilder buildConstraintViolationWithTemplate = context.buildConstraintViolationWithTemplate(message);
        ConstraintValidatorContext addConstraintViolation = buildConstraintViolationWithTemplate.addConstraintViolation();
        return addConstraintViolation;
    }

    /**
     * Get numeric value from application property as BigDecimal where applicationProperty is in format
     * "${some.application.property.file.property.with.numeric}"
     */
    public static BigDecimal propertyValueAsBigDecimal(
            String applicationProperty) {
        String property = resolveProperty(applicationProperty);
        return new BigDecimal(property);
    }

    public static String resolveProperty(
            String applicationProperty) {
        Environment env = AppConfiguration.getApplicationContext().getBean(Environment.class);
        String property = env.resolveRequiredPlaceholders(applicationProperty);
        return property;
    }

    /**
     * Report ConstraintValidator error into given context and provided message template tag and message template
     * parameter.
     */
    public static void reportConstraintViolation(
            ConstraintValidatorContext _context,
            String messageTag,
            String annotationFieldName,
            String fieldValue) {
        Locale locale = LocaleContextHolder.getLocale();
        ApplicationContext applicationContext = AppConfiguration.getApplicationContext();
        ReloadableResourceBundleMessageSource bean = applicationContext.getBean(ReloadableResourceBundleMessageSource.class);
        messageTag = bean.getMessage(messageTag, null, locale);

        HibernateConstraintValidatorContext context = _context.unwrap(HibernateConstraintValidatorContext.class);

        context.disableDefaultConstraintViolation();
        context.addMessageParameter(annotationFieldName, fieldValue);//
        context.buildConstraintViolationWithTemplate(messageTag)//
                .addConstraintViolation();//
    }
}
