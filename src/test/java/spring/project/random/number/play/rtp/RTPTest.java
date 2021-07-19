package spring.project.random.number.play.rtp;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.project.random.number.play.pojo.GameTurn;
import spring.project.random.number.play.pojo.PlayerData;
import spring.project.random.number.play.services.GameCalculationService;
import spring.project.random.number.play.services.PlayerService;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RTPTest {
    @Autowired
    GameCalculationService service;

    @Autowired
    PlayerService playerService;

    @Test
    @RepeatedTest(2)
    void rtpTest() {

        RTPTestWorker worker = new RTPTestWorker(24, 1_000_000);

        worker.setPlayerService(playerService);
        worker.setService(service);
        worker.setGenerateMinimumBet(1);
        worker.setGenerateMaximumBet(50);

        worker.startCalcBlocking(1_000_000);
        int count = count(worker.getGameData(), GameTurn::isPlayerWin);
        assert count > 0;
        calculateStatistics(worker.getGameData(), worker.getPlayerData());

        System.out.println("playerData " + worker.getPlayerData());
        System.out.println("test TotalTime " + worker.getTotalTime());
        System.out.println("test ReturnToPlayerPercent " + worker.getPlayerData().getReturnToPlayerPercent());
        System.out.println("test TotalTime " + String.format("%1$tH:%1$tM:%1$tS.%1$tL", worker.getTotalTime()));

        assertTrue(worker.getPlayerData().getCash() <= 0 || worker.getPlayRounds().get() <= 0);
        assertTrue(worker.getPlayerData().getReturnToPlayerPercent() > 0);
    }

    private static <T> int count(
            List<T> gameData,
            Predicate<T> isPlayerWin) {
        return (int) gameData.stream().filter(isPlayerWin).count();
    }

    private static void calculateStatistics(
            List<GameTurn> gameData,
            PlayerData playerData) {
        long playerWin = count(gameData, GameTurn::isPlayerWin);
        int totalRounds = gameData.size();
        long serverWin = totalRounds - playerWin;
        float winRoundsPercent = 100f * playerWin / serverWin;
        double returnToPlayer = 100 * playerData.getWinCash() / playerData.getLostCash();

        playerData.setWinCount(playerWin);
        playerData.setLoseCount(serverWin);
        playerData.setLoseCount(serverWin);
        playerData.setWinRoundsPercent(winRoundsPercent);
        playerData.setReturnToPlayerPercent(returnToPlayer);

    }
}
