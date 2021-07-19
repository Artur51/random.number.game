package spring.project.random.number.play.validation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import spring.project.random.number.play.validation.pojo.TestMaxPojo;
import spring.project.random.number.play.validation.pojo.TestMaxPojoCustomMessage;
import spring.project.random.number.play.validation.pojo.TestMinPojo;
import spring.project.random.number.play.validation.pojo.TestMinPojoCustomMessage;

@SpringBootTest
public class MinMaxAnnotationsValidatorTest extends AbstractValidationJunitTest {
    @Value("${properties.gameCalculation.maximumNumberInclusive}")
    int maxValue;
    @Value("${properties.gameCalculation.minimumNumberInclusive}")
    int minValue;

    @Test
    public void testMaxAnnotation() {
        validateFailure(new TestMaxPojo(101), "must be less than or equal to " + maxValue);
        validateFailure(new TestMaxPojo(123), "must be less than or equal to " + maxValue);

        validateOk(new TestMaxPojoCustomMessage((maxValue - 1) + ".999999999999999999999999999"));
        validateFailure(new TestMaxPojoCustomMessage("123"), "Maximum value is " + maxValue);
        validateFailure(new TestMaxPojoCustomMessage(maxValue + ".00000000000000000000000001"), "Maximum value is " + maxValue);

        validateOk(new TestMaxPojo(-1));
        validateOk(new TestMaxPojo(maxValue - 100));
        validateOk(new TestMaxPojo(maxValue - 10));
        validateOk(new TestMaxPojo(maxValue));
    }

    @Test
    public void testMinAnnotation() {
        validateFailure(new TestMinPojoCustomMessage((minValue - 1) + ".99999999999999999999999999999999999999999"), "Minimum value is " + minValue);
        validateFailure(new TestMinPojoCustomMessage("-1"), "Minimum value is " + minValue);

        validateFailure(new TestMinPojo(-123), "must be greater than or equal to " + minValue);
        validateFailure(new TestMinPojo(0), "must be greater than or equal to " + minValue);
        validateFailure(new TestMinPojo(-123), "must be greater than or equal to " + minValue);


        validateOk(new TestMinPojo(minValue));
        validateOk(new TestMinPojo(minValue + 10));
        validateOk(new TestMinPojo(minValue + 100));
        validateOk(new TestMinPojo(101));

        validateOk(new TestMinPojoCustomMessage(minValue + ".1"));
        validateOk(new TestMinPojoCustomMessage("" + minValue));
        validateOk(new TestMinPojoCustomMessage("" + (minValue + 10)));
        validateOk(new TestMinPojoCustomMessage("99"));
    }

}