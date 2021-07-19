package spring.project.random.number.play.pojo;

import javax.validation.constraints.PositiveOrZero;

import org.springframework.lang.NonNull;

import spring.project.random.number.play.validation.Max;
import spring.project.random.number.play.validation.Min;

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

    public long getTurnId() {
        return turnId;
    }

    public void setTurnId(
            long turnId) {
        this.turnId = turnId;
    }

    public int getPlayerSelectedNumber() {
        return playerSelectedNumber;
    }

    public void setPlayerSelectedNumber(
            int playerSelectedNumber) {
        this.playerSelectedNumber = playerSelectedNumber;
    }

    public int getServerSelectedNumber() {
        return serverSelectedNumber;
    }

    public void setServerSelectedNumber(
            int serverSelectedNumber) {
        this.serverSelectedNumber = serverSelectedNumber;
    }

    public T getPlayerReward() {
        return playerReward;
    }

    public void setPlayerReward(
            T playerReward) {
        this.playerReward = playerReward;
    }

    public T getServerReward() {
        return serverReward;
    }

    public void setServerReward(
            T serverReward) {
        this.serverReward = serverReward;
    }

    public T getPlayerBet() {
        return playerBet;
    }

    public void setPlayerBet(
            T playerBet) {
        this.playerBet = playerBet;
    }

    public boolean isPlayerWin() {
        return isPlayerWin;
    }

    public void setPlayerWin(
            boolean isPlayerWin) {
        this.isPlayerWin = isPlayerWin;
    }

}
