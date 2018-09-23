package com.bol.service;

import com.bol.game.model.BoardSide;
import com.bol.game.model.Game;
import com.bol.game.model.PlayerTurn;
import com.bol.game.model.Sow;
import com.bol.game.service.GameService;
import com.bol.game.service.SowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SowServiceTest {

    @Mock
    GameService gameService;

    @InjectMocks
    SowService sowService;

    @Test
    public void validSowEndsOnPlayerMainPit() {
        UUID gameId = randomUUID();

        Sow sow = new Sow();
        sow.setPlayerTurn(PlayerTurn.PLAYER_1);
        sow.setPitNumber(0);

        Game newGame = Game.builder().id(gameId).build();

        when(gameService.getGame(gameId)).thenReturn(newGame);
        when(gameService.updateSowedGame(any(), any(), any(), any(), any(), any())).thenReturn(any());

        sowService.sow(gameId, sow);

        verify(gameService, times(1)).updateSowedGame(eq(gameId), any(), any(), eq(sow), eq(BoardSide.PLAYER), eq(6));
    }

    @Test
    public void validSowEndsOnOpponentSide() {
        UUID gameId = randomUUID();

        Sow sow = new Sow();
        sow.setPlayerTurn(PlayerTurn.PLAYER_1);
        sow.setPitNumber(1);

        Game newGame = Game.builder().id(gameId).build();

        when(gameService.getGame(gameId)).thenReturn(newGame);
        when(gameService.updateSowedGame(any(), any(), any(), any(), any(), any())).thenReturn(any());

        sowService.sow(gameId, sow);

        verify(gameService, times(1)).updateSowedGame(eq(gameId), any(), any(), eq(sow), eq(BoardSide.OPPONENT), eq(0));
    }

    @Test
    public void validSowEndsOnPlayerSide() {
        UUID gameId = randomUUID();

        Sow sow = new Sow();
        sow.setPlayerTurn(PlayerTurn.PLAYER_1);
        sow.setPitNumber(0);

        Game newGame = Game.builder().id(gameId).build();
        newGame.getPlayer1().getPits().get(0).setStones(1);

        when(gameService.getGame(gameId)).thenReturn(newGame);
        when(gameService.updateSowedGame(any(), any(), any(), any(), any(), any())).thenReturn(any());

        sowService.sow(gameId, sow);

        verify(gameService, times(1)).updateSowedGame(eq(gameId), any(), any(), eq(sow), eq(BoardSide.PLAYER), eq(1));
    }

    @Test(expected = RuntimeException.class)
    public void invalidSowWrongPlayer() {
        UUID gameId = randomUUID();

        Sow sow = new Sow();
        sow.setPlayerTurn(PlayerTurn.PLAYER_2);
        sow.setPitNumber(0);

        Game newGame = Game.builder().id(gameId).build();
        newGame.getPlayer1().getPits().get(0).setStones(0);

        when(gameService.getGame(gameId)).thenReturn(newGame);

        sowService.sow(gameId, sow);
    }

    @Test(expected = RuntimeException.class)
    public void invalidSowEmptyPit() {
        UUID gameId = randomUUID();

        Sow sow = new Sow();
        sow.setPlayerTurn(PlayerTurn.PLAYER_1);
        sow.setPitNumber(0);

        Game newGame = Game.builder().id(gameId).build();
        newGame.getPlayer1().getPits().get(0).setStones(0);

        when(gameService.getGame(gameId)).thenReturn(newGame);

        sowService.sow(gameId, sow);
    }
}
