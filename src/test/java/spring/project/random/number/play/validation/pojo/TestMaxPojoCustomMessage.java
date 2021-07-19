package spring.project.random.number.play.validation.pojo;


import java.math.BigDecimal;

import org.springframework.validation.annotation.Validated;

import spring.project.random.number.play.validation.Max;

@Validated
public class TestMaxPojoCustomMessage {
    @Max(value = "${properties.gameCalculation.maximumNumberInclusive}", message = "Maximum value is {value}")
    BigDecimal annotatedField;

    public TestMaxPojoCustomMessage(String testValue) {
        this.annotatedField = new BigDecimal(testValue);
    }
}