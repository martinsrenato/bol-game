package com.bol.game.controller;

import com.bol.game.model.Game;
import com.bol.game.service.GameService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class GameController {

    private GameService gameService;

    public GameController(GameService gameService) {
       this.gameService = gameService;
    }

    @GetMapping("/bol-game")
    public String startGame(Model model) {
        Game newGame = gameService.startNewGame();
        log.info("New game started [id={}]", newGame.getId());
        return "bol-game";
    }

}
