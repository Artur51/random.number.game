package spring.project.random.number.play.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PlayerData {
    double returnToPlayerPercent;
    float winRoundsPercent;

    double cash;
    double lostCash;
    double winCash;
    long winCount;
    long loseCount;

    public PlayerData(double cash) {
        this.cash = cash;
    }

    public void addCash(double value) {
        cash += value;
        if (value < 0) {
            lostCash += Math.abs(value);
        } else {
            winCash += value;
        }
    }

}
