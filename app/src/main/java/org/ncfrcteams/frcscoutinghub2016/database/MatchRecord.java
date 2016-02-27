package org.ncfrcteams.frcscoutinghub2016.database;

import java.util.Date;

/**
 * Created by Admin on 2/26/2016.
 */
public class MatchRecord {
    public int TEAMNUM;
    public int MATCHNUM;
    public String scouter = "Undef";
    public Date timeStamp = null;
    public boolean finished = false;

    public MatchRecord(int TEAMNUM, int MATCHNUM) {
        this.TEAMNUM = TEAMNUM;
        this.MATCHNUM = MATCHNUM;
    }

    public boolean equals(Object o) {
        MatchRecord other = (MatchRecord) o;
        return this.TEAMNUM == other.TEAMNUM && this.MATCHNUM == other.MATCHNUM && this.scouter.equals(other.scouter);
    }
}
