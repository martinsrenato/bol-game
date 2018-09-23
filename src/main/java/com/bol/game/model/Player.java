package com.bol.game.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Builder
public class Player implements Serializable {
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

    public Integer getStonesOnMainPit() {
        return this.getMainPit().getStones();
    }

    public Integer getStonesOnPit(Integer pitNumber) {
        return this.getPits().get(pitNumber).getStones();
    }

    public void clearPit(Integer pitNumber) {
        this.getPits().get(pitNumber).clearPit();
    }

    public void addStoneToPit(Integer pitNumber) {
        this.getPits().get(pitNumber).addStone();
    }

    public void addStoneToMainPit() {
        this.getMainPit().addStone();
    }

    public void addStonesToMainPit(Integer newStones) {
        this.getMainPit().addStones(newStones);
    }

    public void finishGame() {
        this.getPits().forEach(pit -> {
            this.getMainPit().addStones(pit.getStones());
            pit.setStones(0);
        });
    }
}
