package spring.project.random.number.play.validation.pojo;


import org.springframework.validation.annotation.Validated;

import spring.project.random.number.play.validation.Max;

@Validated
public class TestMaxPojo {
    @Max(value = "${properties.gameCalculation.maximumNumberInclusive}")
    int annotatedField;

    public TestMaxPojo(int testValue) {
        this.annotatedField = testValue;
    }
}