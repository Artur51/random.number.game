package spring.project.random.number.play.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import spring.project.random.number.play.pojo.GameResult;
import spring.project.random.number.play.pojo.PlayerBetData;
import spring.project.random.number.play.services.GameCalculationService;

import javax.validation.Valid;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {
    @Autowired
    private GameCalculationService service;

    @GetMapping
    @ResponseBody
    public PlayerBetData indexPage() {
        return service.getEmptyBetData();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public GameResult indexPage(
            @RequestBody @Valid PlayerBetData playerBet) {
        return service.playGame(playerBet);
    }
}
