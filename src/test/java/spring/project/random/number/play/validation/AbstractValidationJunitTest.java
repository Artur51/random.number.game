package spring.project.random.number.play.validation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public abstract class AbstractValidationJunitTest {
    private Validator validator;

    @Autowired
    Environment env;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    protected void validateFailure(
            Object testPojo,
            String messagesToValidate) {
        Set<ConstraintViolation<Object>> violations = validator.validate(testPojo);
        assertTrue(!violations.isEmpty());
        messagesToValidate = env.resolveRequiredPlaceholders(messagesToValidate);
        for (ConstraintViolation<Object> cv : violations) {
            String message = cv.getMessage();
            Assertions.assertEquals(messagesToValidate, message);
        }
    }

    protected void validateOk(
            Object testPojo) {
        Set<ConstraintViolation<Object>> violations = validator.validate(testPojo);
        assertTrue(violations.isEmpty());
    }
}
