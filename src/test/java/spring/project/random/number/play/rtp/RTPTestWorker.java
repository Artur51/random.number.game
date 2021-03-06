package spring.project.random.number.play.rtp;

import spring.project.random.number.play.pojo.GameTurn;
import spring.project.random.number.play.pojo.PlayerData;
import spring.project.random.number.play.services.GameCalculationService;
import spring.project.random.number.play.services.PlayerService;
import spring.project.random.number.play.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class RTPTestWorker {
    ExecutorService ex;
    List<GameTurn> gameData = Collections.synchronizedList(new ArrayList());
    PlayerData playerData = new PlayerData();

    public List<GameTurn> getGameData() {
        return gameData;
    }

    public GameCalculationService getService() {
        return service;
    }

    public void setService(GameCalculationService service) {
        this.service = service;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public PlayerService getPlayerService() {
        return playerService;
    }

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public int getCores() {
        return cores;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }

    public int getGenerateMinimumBet() {
        return generateMinimumBet;
    }

    public void setGenerateMinimumBet(int generateMinimumBet) {
        this.generateMinimumBet = generateMinimumBet;
    }

    public int getGenerateMaximumBet() {
        return generateMaximumBet;
    }

    public void setGenerateMaximumBet(int generateMaximumBet) {
        this.generateMaximumBet = generateMaximumBet;
    }

    public AtomicInteger getPlayRounds() {
        return playRounds;
    }

    public void setPlayRounds(AtomicInteger playRounds) {
        this.playRounds = playRounds;
    }

    GameCalculationService service;
    PlayerService playerService;
    int cores;
    int generateMinimumBet;
    int generateMaximumBet;
    AtomicInteger playRounds;
    long totalTime;

    public RTPTestWorker(int cores, int playerCash) {
        this.cores = cores;
        playerData.setCash(playerCash);
        ex = Executors.newScheduledThreadPool(cores);
    }

    Object lock = new Object();
    volatile boolean isDone = false;

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            try {
                if (isDone) {
                    return;
                }
                if (playerService.noCash(playerData) || playRounds.getAndDecrement() <= 0) {
                    isDone = true;
                    ex.shutdownNow();
                    Utils.notifyAll(lock);
                    return;
                }
                BigDecimal bet = playerService.getRandomBet(generateMinimumBet, generateMaximumBet);
                int playerNumber = playerService.generatePlayerNumber();

                GameTurn gameTurn = service.playRound(bet, playerNumber);
                gameData.add(gameTurn);
                playerService.updateData(playerData, gameTurn);

                if (isDone) {
                    return;
                }
                ex.execute(this);
            } catch (Exception e) {
                e.printStackTrace();
                isDone = true;
                ex.shutdownNow();
                Utils.notifyAll(lock);
            }
        }
    };

    /**
     * We start per core task count and as soon as task is completed it will add new task at the end.
     */
    public void startCalcBlocking(
            int playRounds) {
        long startTime = System.currentTimeMillis();
        this.playRounds = new AtomicInteger(playRounds);
        for (int i = 0, size = Math.min(cores, playRounds); i < size; i++) {
            ex.execute(task);
        }
        Utils.wait(lock);
        Utils.shutdownAndWait(ex);
        totalTime = System.currentTimeMillis() - startTime;
    }
}