package com.bol.game.controller;

import com.bol.game.model.Game;
import com.bol.game.model.Sow;
import com.bol.game.service.GameService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.UUID.randomUUID;

@Log4j2
@Controller
public class GameController {

    private GameService gameService;

    public GameController(GameService gameService) {
       this.gameService = gameService;
    }

    @GetMapping("/bol-game")
    public String getGame(
            @CookieValue(value = "game_id", required = false) UUID gameId,
            HttpServletResponse response,
            Model model) {
        if(gameId == null) {
            return getGame(response, model);
        }

        Game game = gameService.getGame(gameId);
        model.addAttribute("game", game);
        model.addAttribute("sow", new Sow());

        return "bol-game";
    }

    @GetMapping("/bol-game:restart")
    public String getGame(HttpServletResponse response, Model model) {
        UUID gameId = randomUUID();
        log.info("Starting new game [id={}]", gameId);

        Cookie cookie = new Cookie("game_id", gameId.toString());
        cookie.setMaxAge(Long.valueOf(TimeUnit.DAYS.toSeconds(1)).intValue());

        response.addCookie(cookie);

        Game game = gameService.getGame(gameId);
        model.addAttribute("game", game);
        model.addAttribute("sow", new Sow());

        return "redirect:/bol-game";
    }
}
