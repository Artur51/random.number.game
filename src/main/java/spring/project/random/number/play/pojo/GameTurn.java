package spring.project.random.number.play.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.PositiveOrZero;

import spring.project.random.number.play.validation.Max;
import spring.project.random.number.play.validation.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GameTurn<T extends Number> {

    @PositiveOrZero
    long turnId;
    @Min(value = "${properties.gameCalculation.minimumNumberInclusive}")
    @Max(value = "${properties.gameCalculation.maximumNumberInclusive}")
    int playerSelectedNumber;
    @Min(value = "${properties.gameCalculation.minimumNumberInclusive}")
    @Max(value = "${properties.gameCalculation.maximumNumberInclusive}")
    int serverSelectedNumber;

    @NonNull
    T playerReward;
    @NonNull
    T serverReward;
    @NonNull
    T playerBet;

    boolean isPlayerWin;
}
