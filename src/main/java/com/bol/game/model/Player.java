package com.bol.game.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Value
@Builder
public class Player {
    @Builder.Default
    private List<Pit> pits = Stream.of(Pit.builder().stones(6).build(),
                                        Pit.builder().stones(6).build(),
                                        Pit.builder().stones(6).build(),
                                        Pit.builder().stones(6).build(),
                                        Pit.builder().stones(6).build(),
                                        Pit.builder().stones(6).build())
                                    .collect(Collectors.toList());
    @Builder.Default
    private Pit mainPit = Pit.builder().stones(0).build();
}
