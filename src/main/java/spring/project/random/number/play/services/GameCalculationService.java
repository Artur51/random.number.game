package spring.project.random.number.play.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import spring.project.random.number.play.pojo.GameResult;
import spring.project.random.number.play.pojo.GameTurn;
import spring.project.random.number.play.pojo.PlayerBetData;
import spring.project.random.number.play.utils.CalcUtils;
import spring.project.random.number.play.utils.Utils;
import spring.project.random.number.play.validation.Max;
import spring.project.random.number.play.validation.Min;

@Service
@Validated
public class GameCalculationService {
    private final ThreadLocal<Random> threadLocalForServerRandom = ThreadLocal.withInitial(Random::new);

    @Value("${properties.gameCalculation.minimumNumberInclusive}")
    private int minimumNumber;
    @Value("${properties.gameCalculation.maximumNumberInclusive}")
    private int maximumNumber;

    public static final MathContext DEFAULT_PRECISION = MathContext.DECIMAL128;
    public static final BigDecimal ZERO_REWARD = new BigDecimal("0.0");

    AtomicLong turnCounter = new AtomicLong();

    @Autowired
    @Value("${properties.betChangeFormulaConstant1}")
    private BigDecimal conts1;
    @Autowired
    @Value("${properties.betChangeFormulaConstant2}")
    private BigDecimal conts2;

    public long getTurnCounter() {
        return turnCounter.get();
    }

    public GameResult playGame(
            @Valid PlayerBetData playerBet) {
        GameTurn<BigDecimal> result = playRound(playerBet.getBet(), playerBet.getNumber());

        if (result.isPlayerWin()) {
            return new GameResult(result.getPlayerReward(), true);
        }
        return new GameResult(null, false);
    }

    /**
     * Server generates random number 1 - 99. If player number is bigger then server generated one then reward is
     * calculated as the response.
     */
    public GameTurn<BigDecimal> playRound(
            @NotNull @Positive
            BigDecimal bet, //
            @Min("${properties.gameCalculation.minimumNumberInclusive}") //
            @Max("${properties.gameCalculation.maximumNumberInclusive}")
            int playerNumber) {
        long turnId = turnCounter.getAndIncrement();
        BigDecimal betReward = bet;
        Utils.validate(betReward.doubleValue() >= 0, "Invalid bet count " + betReward);
        int serverNumber = generateServerNumber();

        BigDecimal rewardToPlayer = ZERO_REWARD;
        BigDecimal rewardToServer = ZERO_REWARD;
        boolean isPlayerWin = playerNumber > serverNumber;

        if (isPlayerWin) {
            rewardToPlayer = getReward(betReward, playerNumber);
        } else {
            rewardToServer = betReward;
        }

        GameTurn<BigDecimal> gameTurn = new GameTurn<BigDecimal>();
        gameTurn.setTurnId(turnId);
        gameTurn.setServerSelectedNumber(serverNumber);
        gameTurn.setPlayerSelectedNumber(playerNumber);
        gameTurn.setPlayerReward(rewardToPlayer);
        gameTurn.setServerReward(rewardToServer);
        gameTurn.setPlayerWin(isPlayerWin);
        gameTurn.setPlayerBet(betReward);

        Utils.validate(gameTurn);
        return gameTurn;
    }

    public BigDecimal getReward(
            String bet,
            int number) {
        BigDecimal value = new BigDecimal(bet);
        return getReward(value, number);
    }


    public int generateServerNumber() {
        Random r = threadLocalForServerRandom.get();
        return CalcUtils.getRandom(r, minimumNumber, maximumNumber);
    }

    /**
     * Reward calculation method.
     * Reward depends on chance - = bet * (99 / (100 - number)),
     * If player has select number 50 and bet is 40.5, the reward must be 80.19
     */
    public BigDecimal getReward(
            BigDecimal bet,
            int number) {
        BigDecimal result = null;
        try {
            BigDecimal valueNumber = new BigDecimal(number, DEFAULT_PRECISION);
            BigDecimal coef1 = conts2.subtract(valueNumber);// 100
            BigDecimal coef2 = conts1.divide(coef1, DEFAULT_PRECISION);
            result = bet.multiply(coef2);
        } catch (ArithmeticException e) {
            Utils.throwRuntime("ArithmeticException with parameters bet " + bet + " number " + number, e);
        }
        return result;
    }

    public PlayerBetData getEmptyBetData() {
        return new PlayerBetData(new BigDecimal(0), minimumNumber);
    }
}
