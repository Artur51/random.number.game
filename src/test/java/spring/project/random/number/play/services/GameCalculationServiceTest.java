package spring.project.random.number.play.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import spring.project.random.number.play.pojo.GameTurn;

@SpringBootTest
class GameCalculationServiceTest {

    @Autowired
    GameCalculationService service;

    @Value("${properties.gameCalculation.minimumNumberInclusive}")
    private int minimumNumber;
    @Value("${properties.gameCalculation.maximumNumberInclusive}")
    private int maximumNumber;

    @Test
    void testGetReward() {
        BigDecimal reward = service.getReward(new BigDecimal("40.5"), 50);
        assertEquals(new BigDecimal("80.190"), reward);

        reward = service.getReward(new BigDecimal("10.5"), 20);
        assertEquals(new BigDecimal("12.99375"), reward);

        reward = service.getReward(new BigDecimal("1"), 10);
        assertEquals(new BigDecimal("1.1"), reward);
    }

    @Autowired
    @Value("${properties.betChangeFormulaConstant2}")
    private BigDecimal conts2;

    @Test
    void testArithmeticException() {
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            BigDecimal reward = service.getReward(new BigDecimal("40.5"), conts2.intValue());
        });

        assertTrue(thrown.getMessage().contains("ArithmeticException"));
    }

    @Test
    @RepeatedTest(100)
    void testPlayGameWin() {
        GameTurn<BigDecimal> playRound = service.playRound(new BigDecimal("40.5"), maximumNumber);
        int serverSelectedNumber = playRound.getServerSelectedNumber();
        if (serverSelectedNumber < maximumNumber) {
            assertTrue(playRound.isPlayerWin());
            assertNotNull(playRound.getPlayerReward());
        } else {
            assertFalse(playRound.isPlayerWin());
            assertNotNull(playRound.getServerReward());
        }
    }

    @Test
    @RepeatedTest(100)
    void testPlayGameLose() {
        GameTurn<BigDecimal> playRound = service.playRound(new BigDecimal("40.5"), minimumNumber);
        int serverSelectedNumber = playRound.getServerSelectedNumber();
        if (serverSelectedNumber == minimumNumber) {
            if (playRound.getPlayerSelectedNumber() == minimumNumber) {
                assertFalse(playRound.isPlayerWin());
                assertNotNull(playRound.getServerReward());
            } else {
                assertTrue(playRound.isPlayerWin());
                assertNotNull(playRound.getPlayerReward());
            }
        } else {
            assertFalse(playRound.isPlayerWin());
            assertNotNull(playRound.getServerReward());
        }
    }

    @Test
    void testPlayRoundMethodInvalidParametrNumberIsBig() {
        ConstraintViolationException thrown = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            service.playRound(new BigDecimal("40.5"), 1000);
        });
        assertEquals("playRound.playerNumber: must be less than or equal to " + maximumNumber, thrown.getMessage());
    }

    @Test
    void testPlayRoundMethodInvalidParametrBetIsNull() {
        ConstraintViolationException thrown = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            service.playRound(null, 10);
        });
        assertEquals("playRound.bet: must not be null", thrown.getMessage());
    }

    @Test
    void testPlayRoundMethodInvalidParametrBetIsNegative() {
        ConstraintViolationException thrown = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            service.playRound(new BigDecimal("-1"), 12);
        });
        assertEquals("playRound.bet: must be greater than 0", thrown.getMessage());
    }

    @Test
    void testPlayRoundMethodInvalidParametrNumberIsNegative() {
        ConstraintViolationException thrown = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            service.playRound(new BigDecimal("10"), -10);
        });
        assertEquals("playRound.playerNumber: must be greater than or equal to " + minimumNumber, thrown.getMessage());
    }
}