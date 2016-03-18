package org.ncfrcteams.frcscoutinghub2016.matchdata;

/**
 * Created by Kyle Brown on 3/17/2016.
 */
public enum Obstacle {
    ROUGH_TERRAIN(0),ROCK_WALL(1),DRAW_BRIDGE(2),SALLY_PORT(3),PORTCULLIS(4),CHIVEL_DE_FRISE(5),MOAT(6),RAMPARTS(7);

    private int value;

    Obstacle(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
