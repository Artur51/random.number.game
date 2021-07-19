package spring.project.random.number.play.pojo;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import spring.project.random.number.play.validation.Max;
import spring.project.random.number.play.validation.Min;

public class PlayerBetData {
    @NotNull
    @Positive
    private BigDecimal bet;

    public PlayerBetData() {
    }

    public PlayerBetData(@Valid @NotNull @Positive BigDecimal bet, @Valid @Min("${properties.gameCalculation.minimumNumberInclusive}") @Max("${properties.gameCalculation.maximumNumberInclusive}") int number) {
        this.bet = bet;
        this.number = number;
    }

    @Min("${properties.gameCalculation.minimumNumberInclusive}")
    @Max("${properties.gameCalculation.maximumNumberInclusive}")
    private int number;

    public BigDecimal getBet() {
        return bet;
    }

    public void setBet(
            BigDecimal bet) {
        this.bet = bet;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(
            int number) {
        this.number = number;
    }
}
