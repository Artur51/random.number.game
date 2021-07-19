package spring.project.random.number.play.validation.pojo;


import org.springframework.validation.annotation.Validated;

import spring.project.random.number.play.validation.Min;

@Validated
public class TestMinPojo {

    @Min("${properties.gameCalculation.minimumNumberInclusive}")
    int annotatedField;


    public TestMinPojo(int testValue) {
        this.annotatedField = testValue;
    }

}