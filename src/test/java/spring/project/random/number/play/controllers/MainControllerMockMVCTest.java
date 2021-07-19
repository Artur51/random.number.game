package spring.project.random.number.play.controllers;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import spring.project.random.number.play.pojo.PlayerBetData;
import spring.project.random.number.play.services.GameCalculationService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MainControllerMockMVCTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Value("${properties.gameCalculation.maximumNumberInclusive}")
    int maxValue;
    @Value("${properties.gameCalculation.minimumNumberInclusive}")
    int minValue;
    @MockBean
    private GameCalculationService mackService;

    String invalidJson = "{\"someName\":\"ABCD\"}";
    String validJson = "{\"bet\":9,\"number\":10}";

    @Test
    public void testSendInvalidJsonShouldError400() throws Exception {

        mockMvc.perform(post("/") //
                .content(invalidJson) //
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)) //
                .andDo(print()) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.timestamp", is(notNullValue()))) //
                .andExpect(jsonPath("$.status", is(400))) //
                .andExpect(jsonPath("$.errors").isArray()) //
                .andExpect(jsonPath("$.errors", hasSize(2))) //
                .andExpect(jsonPath("$.errors", hasItem("must be greater than or equal to " + minValue))) //
        ;
        verify(mackService, times(0)).playGame(any(PlayerBetData.class));
    }

    @Test
    public void testSendValidJsonShouldOk() throws Exception {

        mockMvc.perform(post("/") //
                .content(validJson) //
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)) //
                .andDo(print()) //
                .andExpect(status().isOk()) //
        ;

        verify(mackService, times(1)).playGame(any(PlayerBetData.class));
    }

    @Test
    public void testInvalidBigNumberShouldHaveErrorMessage() throws JsonProcessingException, Exception {

        PlayerBetData body = new PlayerBetData();
        body.setBet(new BigDecimal("10"));
        body.setNumber(101);

        String json = mapper.writeValueAsString(body);

        mockMvc.perform(MockMvcRequestBuilders.post("/")//
                .content(json) //
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isBadRequest())//
                .andDo(print())//
                .andExpect(jsonPath("$.errors", hasSize(1)))//
                .andExpect(jsonPath("$.errors", hasItem("must be less than or equal to " + maxValue)));
    }

    @Test
    public void testInvalidSmallNumberShouldHaveErrorMessage() throws JsonProcessingException, Exception {

        PlayerBetData body = new PlayerBetData();
        body.setBet(new BigDecimal("10"));
        body.setNumber(0);

        String json = mapper.writeValueAsString(body);

        mockMvc.perform(MockMvcRequestBuilders.post("/")//
                .content(json) //
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isBadRequest())//
                .andDo(print())//
                .andExpect(jsonPath("$.errors", hasSize(1)))//
                .andExpect(jsonPath("$.errors", hasItem("must be greater than or equal to " + minValue)));
    }

    @Test
    public void testPostRequestWithoutBody() throws JsonProcessingException, Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/")//
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isBadRequest())//
                .andDo(print())//
                .andExpect((
                        result) -> {
                    String message = result.getResolvedException().getMessage();
                    assertTrue(message.startsWith("Required request body is missing: "));
                })//
        ;
    }
}
