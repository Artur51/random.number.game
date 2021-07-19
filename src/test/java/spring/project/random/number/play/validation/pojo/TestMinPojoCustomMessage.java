package spring.project.random.number.play.validation.pojo;


import java.math.BigDecimal;

import org.springframework.validation.annotation.Validated;

import spring.project.random.number.play.validation.Min;

@Validated
public class TestMinPojoCustomMessage {

    @Min(value = "${properties.gameCalculation.minimumNumberInclusive}", message = "Minimum value is {value}")
    BigDecimal annotatedField;


    public TestMinPojoCustomMessage(String testValue) {
        this.annotatedField = new BigDecimal(testValue);
    }

}