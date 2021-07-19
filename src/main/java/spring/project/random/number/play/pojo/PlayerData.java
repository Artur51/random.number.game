package spring.project.random.number.play.pojo;

public class PlayerData {
    double returnToPlayerPercent;
    float winRoundsPercent;

    double cash;
    double lostCash;
    double winCash;
    long winCount;
    long loseCount;

    public PlayerData() {
    }
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

    public double getReturnToPlayerPercent() {
        return returnToPlayerPercent;
    }

    public void setReturnToPlayerPercent(
            double returnToPlayerPercent) {
        this.returnToPlayerPercent = returnToPlayerPercent;
    }

    public float getWinRoundsPercent() {
        return winRoundsPercent;
    }

    public void setWinRoundsPercent(
            float winRoundsPercent) {
        this.winRoundsPercent = winRoundsPercent;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(
            double cash) {
        this.cash = cash;
    }

    public double getLostCash() {
        return lostCash;
    }

    public void setLostCash(
            double lostCash) {
        this.lostCash = lostCash;
    }

    public double getWinCash() {
        return winCash;
    }

    public void setWinCash(
            double winCash) {
        this.winCash = winCash;
    }

    public long getWinCount() {
        return winCount;
    }

    public void setWinCount(
            long winCount) {
        this.winCount = winCount;
    }

    public long getLoseCount() {
        return loseCount;
    }

    public void setLoseCount(
            long loseCount) {
        this.loseCount = loseCount;
    }

}
