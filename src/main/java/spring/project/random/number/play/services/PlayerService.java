package spring.project.random.number.play.services;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import spring.project.random.number.play.pojo.GameTurn;
import spring.project.random.number.play.pojo.PlayerData;
import spring.project.random.number.play.utils.CalcUtils;
import spring.project.random.number.play.utils.Utils;

@Service
public class PlayerService {

    private final ThreadLocal<Random> threadLocalForRandomBet = ThreadLocal.withInitial(Random::new);
    private final ThreadLocal<Random> threadLocalForPlayerRandom = ThreadLocal.withInitial(Random::new);

    @Value("${properties.gameCalculation.minimumNumberInclusive}")
    private int minimumNumber;
    @Value("${properties.gameCalculation.maximumNumberInclusive}")
    private int maximumNumber;


    public int generatePlayerNumber() {
        Random r = threadLocalForPlayerRandom.get();
        return CalcUtils.getRandom(r, minimumNumber, maximumNumber);
    }
    public BigDecimal getRandomBet(
            int min,
            int maxInclusive) {
        Random random2 = threadLocalForRandomBet.get();
        double randomValue = CalcUtils.getRandom(random2, min, maxInclusive);
        return new BigDecimal(randomValue);
    }

    public void updateData(PlayerData player, GameTurn gameTurn) {
        Utils.validate(gameTurn);
        if (gameTurn.isPlayerWin()) {
            player.addCash(gameTurn.getPlayerReward().doubleValue());
        } else {
            Number playerBet = gameTurn.getPlayerBet();
            Objects.requireNonNull(playerBet);
            assert playerBet.doubleValue() > 0;
            double value = -gameTurn.getPlayerBet().doubleValue();
            player.addCash(value);
        }
    }

    public boolean noCash(PlayerData player) {
        return player.getCash() <= 0;
    }
}