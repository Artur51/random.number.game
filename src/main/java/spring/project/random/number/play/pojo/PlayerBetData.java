package spring.project.random.number.play.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import spring.project.random.number.play.validation.Max;
import spring.project.random.number.play.validation.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlayerBetData {
    @NotNull
    @Positive
    private BigDecimal bet;

    @Min("${properties.gameCalculation.minimumNumberInclusive}")
    @Max("${properties.gameCalculation.maximumNumberInclusive}")
    private int number;
}
