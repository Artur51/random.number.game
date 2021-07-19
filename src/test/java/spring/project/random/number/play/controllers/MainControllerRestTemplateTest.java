package spring.project.random.number.play.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import spring.project.random.number.play.pojo.GameResult;
import spring.project.random.number.play.pojo.PlayerBetData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MainControllerRestTemplateTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Value("${properties.gameCalculation.minimumNumberInclusive}")
    public int minimumNumber;
    @Value("${properties.gameCalculation.maximumNumberInclusive}")
    public int maximumNumber;

    @Test
    public void testGetRequestEmptyObject() {
        PlayerBetData body = restTemplate.getForObject("/", PlayerBetData.class);
        assertEquals(body.getBet().doubleValue(), 0.0);
        assertEquals(body.getNumber(), minimumNumber);
    }

    @Test
    public void testPostRequestValidObject() {
        PlayerBetData body = restTemplate.getForObject("/", PlayerBetData.class);
        body.setBet(new BigDecimal("10"));
        body.setNumber(10);

        GameResult body2 = restTemplate.postForObject("/", body, GameResult.class);
        if (body2.isPlayerWin()) {
            assertNotNull(body2.getReward());
        } else {
            assertNull(body2.getReward());
        }
    }

    @Test
    public void testPostRequestInvalidObject() {
        PlayerBetData body = restTemplate.getForObject("/", PlayerBetData.class);
        body.setBet(new BigDecimal("10"));
        body.setNumber(101);

        PlayerBetData body2 = restTemplate.postForObject("/", body, PlayerBetData.class);
        assertNotEquals(body.getBet(), body2.getBet());
        assertNotEquals(body.getNumber(), body2.getNumber());
    }

}
