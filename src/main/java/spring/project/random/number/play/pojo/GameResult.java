package spring.project.random.number.play.pojo;

import java.math.BigDecimal;

public class GameResult {

    BigDecimal reward;
    boolean isPlayerWin;

    public GameResult() {
    }

    public GameResult(BigDecimal reward, boolean isPlayerWin) {
        this.reward = reward;
        this.isPlayerWin = isPlayerWin;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(
            BigDecimal reward) {
        this.reward = reward;
    }

    public boolean isPlayerWin() {
        return isPlayerWin;
    }

    public void setPlayerWin(
            boolean isPlayerWin) {
        this.isPlayerWin = isPlayerWin;
    }


}
