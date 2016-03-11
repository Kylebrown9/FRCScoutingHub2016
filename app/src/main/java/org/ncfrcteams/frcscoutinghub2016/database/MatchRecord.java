package org.ncfrcteams.frcscoutinghub2016.database;

/**
 * Created by Kyle Brown on 3/10/2016.
 */
public class MatchRecord extends SuperMap<String, Integer>{

    public static MatchRecord createMatchRecord(String data, String orientation){
        String[] parts = data.split(",");
        if(parts.length != 11)
            return null;

        MatchRecord newMatchRecord = new MatchRecord();

        try {
            newMatchRecord.put("Color", (parts[0].equals("B") ? 1 : 2));
            newMatchRecord.put("Match Number", Integer.parseInt(parts[1]));
            newMatchRecord.put("Team Number", Integer.parseInt(parts[2]));
            for(int i = 1; i < 9; i++){
                newMatchRecord.put("Barrier " + i, Integer.parseInt(parts[i + 2]));
            }

            if(orientation.equals("blue-on-left")) {
                newMatchRecord.put("Orientation", 1);
            } else {
                newMatchRecord.put("Orientation", 2);
            }

            return newMatchRecord;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public synchronized String getCompressed(){
        //4 bits each:
        // HighAuto, HighAutoTotal, LowAuto, LowAutoTotal,
        // HighTele, HighTeleTotal, LowTele, LowTeleTotal,
        // A1, A2, B1, B2, C1, C2, D1, D2, Low
        // challenged and or captured

        return "hi";
    }

}
