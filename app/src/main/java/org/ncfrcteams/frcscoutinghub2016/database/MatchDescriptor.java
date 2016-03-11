package org.ncfrcteams.frcscoutinghub2016.database;

import java.io.Serializable;

/**
 * Created by Kyle Brown on 3/10/2016.
 */
public class MatchDescriptor implements Serializable {
    private int matchNum = 0;
    private int[] teams;
    private Obstacle[] obstacles = new Obstacle[8];

    public MatchDescriptor(int matchNum, int[] teams) {
        this.matchNum = matchNum;
        this.teams = teams;
    }

    public static MatchDescriptor fromString(String s) {
        String[] parts = s.split(",");
        int matchNum = Integer.parseInt(parts[0]);
        int[] teams = new int[6];
        for(int i=1; i<parts.length; i++) {
            teams[i-1] = Integer.parseInt(parts[i]);
        }
        return new MatchDescriptor(matchNum,teams);
    }

    public String toString(Team t) {
        StringBuilder s = new StringBuilder();

        s.append((t.getValue() > 2)? "B" : "R");
        s.append(",");
        s.append(matchNum);
        s.append(",");
        s.append(teams[t.getValue()]);

        for(Obstacle o : obstacles) {
            s.append(",");
            s.append(o.getValue());
        }

        return s.toString();
    }

    public String[] getStrings() {
        return new String[] {
                toString(Team.R1),
                toString(Team.R2),
                toString(Team.R3),
                toString(Team.B1),
                toString(Team.B2),
                toString(Team.B3)
        };
    }

    public int getMatchNum() {
        return matchNum;
    }

    public int getTeamNum(Team t) {
        return teams[t.getValue()];
    }

    public synchronized void setObstacles(Obstacle[] o) {
        obstacles = o;
    }

    public synchronized Obstacle[] getObstacles() {
        return obstacles.clone();
    }

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

    public enum Team {
        R1(0),R2(1),R3(2),B1(3),B2(4),B3(5);

        private int value;

        Team(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
