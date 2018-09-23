package com.bol.game.service;

import com.bol.game.model.BoardSide;
import com.bol.game.model.Game;
import com.bol.game.model.Player;
import com.bol.game.model.Sow;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.bol.game.model.BoardSide.*;

@Service
public class SowService {

    GameService gameService;

    public SowService(GameService gameService) {
        this.gameService = gameService;
    }

    public void sow(UUID gameId, Sow sow) {
        Game game = gameService.getGame(gameId);

        if(!game.getPlayerTurn().equals(sow.getPlayerTurn())) {
            throw new RuntimeException("Invalid sow - Not player's turn");
        }

        Player player = game.getPlayer(sow.getPlayerTurn());
        Player opponent = game.getOpponent(sow.getPlayerTurn());

        Integer stones = player.getStonesOnPit(sow.getPitNumber());
        player.clearPit(sow.getPitNumber());

        if(stones == 0) {
            throw new RuntimeException("Invalid sow - Pit is empty");
        }

        BoardSide side = null;
        Integer lastPitSowed = null;

        while(stones > 0) {
            //player side
            side = PLAYER;
            lastPitSowed = sow.getPitNumber();
            for(int i = sow.getPitNumber() + 1; i < player.getPits().size() && stones > 0; i++) {
                player.addStoneToPit(i);
                lastPitSowed = i;
                stones--;
            }

            //player main pit
            if(stones > 0) {
                player.addStoneToMainPit();
                lastPitSowed++;
                stones--;
            }

            //opponent side
            if(stones > 0) {
                side = OPPONENT;
                for(int i = 0; i < opponent.getPits().size() && stones > 0; i++) {
                    opponent.addStoneToPit(i);
                    lastPitSowed = i;
                    stones--;
                }
            }

            //player side - again!
            if(stones > 0) {
                side = PLAYER;
                for(int i = 0; i <= sow.getPitNumber() && stones > 0; i++) {
                    player.addStoneToPit(i);
                    lastPitSowed = i;
                    stones--;
                }
            }
        }

        gameService.updateSowedGame(gameId, player, opponent, sow, side, lastPitSowed);
    }
}
