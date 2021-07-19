package spring.project.random.number.play.validation;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import spring.project.random.number.play.pojo.PlayerBetData;

@SpringBootTest
public class PlayerBetDataTest extends AbstractValidationJunitTest {
    @Value("${properties.gameCalculation.minimumNumberInclusive}")
    private int minimumNumber;
    @Value("${properties.gameCalculation.maximumNumberInclusive}")
    private int maximumNumber;

    @Test
    public void testParametersValiadtion() {
        validateFailure(new PlayerBetData(null, 10), "must not be null");
        validateOk(new PlayerBetData(new BigDecimal(12), 10));

        validateFailure(new PlayerBetData(new BigDecimal(12), -1), "must be greater than or equal to " + minimumNumber);
        validateFailure(new PlayerBetData(new BigDecimal(12), 101), "must be less than or equal to " + maximumNumber);
    }
}