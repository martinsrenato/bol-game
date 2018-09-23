package com.bol.game.controller;

import com.bol.game.model.Sow;
import com.bol.game.service.SowService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Log4j2
@Controller
@RequestMapping(value = "/bol-game/sow")
public class SowController {

    SowService sowService;

    public SowController(SowService sowService) {
        this.sowService = sowService;
    }

    @PostMapping
    public String sow(
            @CookieValue(value = "game_id") UUID gameId,
            @ModelAttribute Sow sow) {
        log.info("{} [gameId={}]", sow, gameId);
        sowService.sow(gameId, sow);
        return "redirect:/bol-game";
    }
}
