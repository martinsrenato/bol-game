package com.bol.game.model;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

import static java.util.UUID.randomUUID;

@Value
@Builder
public class Game {

    @Builder.Default
    private UUID id = randomUUID();
    private GameState gameState;
    @Builder.Default
    private Player player1 = Player.builder().build();
    @Builder.Default
    private Player player2 = Player.builder().build();
}
