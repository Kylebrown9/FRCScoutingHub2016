package org.ncfrcteams.frcscoutinghub2016.matchdata.database;

/**
 * Created by Kyle Brown on 3/10/2016.
 */
public class MatchRecord extends SuperMap<String, Integer>{

    private String activeButton = "None";
    private int mode;
    private int color;
    private int orientation;
    private int[] barriers = {0,0,0,0,0,0,0,0,0,0};

    public static MatchRecord createMatchRecord(String data, int orientation){
        String[] parts = data.split(",");
        if(parts.length != 11)
            return null;

        MatchRecord newMatchRecord = new MatchRecord();

        try {
            newMatchRecord.put("Color", (parts[0].equals("B") ? 1 : 2)); //not put in supermap
            newMatchRecord.put("Match Number", Integer.parseInt(parts[1]));
            newMatchRecord.put("Team Number", Integer.parseInt(parts[2]));
            newMatchRecord.put("Orientation", orientation); //not put in super map
            newMatchRecord.put("Teleop Active", 0);

            for(int i = 1; i < 9; i++){
                newMatchRecord.barriers[i] = Integer.parseInt(parts[i + 2]);
            }

            if (orientation == (parts[0].equals("R") ? 1 : 2)){
                newMatchRecord.put("Mode", 1); //not put in supermap
            } else{
                newMatchRecord.put("Mode", 2); //not put in supermap
            }

            newMatchRecord.put("Shoot High Auto",0);
            newMatchRecord.put("Shoot High Auto Total",0);
            newMatchRecord.put("Shoot Low Auto",0);
            newMatchRecord.put("Shoot Low Auto Total",0);
            newMatchRecord.put("Shoot High Tele",0);
            newMatchRecord.put("Shoot High Tele Total",0);
            newMatchRecord.put("Shoot Low Tele",0);
            newMatchRecord.put("Shoot Low Tele Total",0);
            newMatchRecord.put("Block High Auto",0);
            newMatchRecord.put("Block High Auto Total",0);
            newMatchRecord.put("Block Low Auto",0);
            newMatchRecord.put("Block Low Auto Total",0);
            newMatchRecord.put("Block High Tele",0);
            newMatchRecord.put("Block High Tele Total",0);
            newMatchRecord.put("Block Low Tele",0);
            newMatchRecord.put("Block Low Tele Total",0);

            newMatchRecord.clearHistory();

            return newMatchRecord;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public synchronized Integer get(String s){
        switch(s){
            case "Mode":
                return mode;
            case "Color":
                return color;
            case "Orientation":
                return orientation;
            default:
                return super.get(s);
        }
    }

    public synchronized void put(String s, int i){
        switch(s){
            case "Mode":
                mode = i;
            case "Color":
                color = i;
            case "Orientation":
                orientation = i;
            default:
                super.put(s, i);
        }
    }

    public synchronized void setActiveButton(String button){
        activeButton = button;
    }

    public synchronized String getActiveButton(){
        return activeButton;
    }

    public synchronized int[] getBarriers(){
        return barriers;
    }

    public synchronized String findKey(String button){
        String mykey;
        switch (button) {
            case "Left High":
                mykey = get("Mode").equals(1) ? "Shoot High" : "Block High";
                return mykey + (get("Teleop Active").equals(0) ? " Auto" : " Tele");
            case "Left Low":
                mykey = get("Mode").equals(1) ? "Shoot Low" : "Block Low";
                return mykey + (get("Teleop Active").equals(0) ? " Auto" : " Tele");
            case "Right High":
                mykey = get("Mode").equals(2) ? "Shoot High" : "Block High";
                return mykey + (get("Teleop Active").equals(0) ? " Auto" : " Tele");
            case "Right Low":
                mykey = get("Mode").equals(2) ? "Shoot Low" : "Block Low";
                return mykey + (get("Teleop Active").equals(0) ? " Auto" : " Tele");
            default:
                return "None";
        }
    }

    public synchronized void increment(String button, int val) {
        if (! button.equals("None")){
            String key = findKey(button);
            put(key, get(key) + val);
            put(key + " Total", get(key + " Total") + 1);
        }
    }

    public synchronized String getString(String button) {
        if (! button.equals("None")) {
            String key = findKey(button);
            int top = get(key);
            int bottom = get(key + " Total");
            String beg = key.substring(0, key.length() - 5);
            return beg + ":\n" + top + "/" + bottom;
        }
        return "None";
    }

//

    public synchronized String getCompressed(){
        return "hi";
    }
}

//    public static MatchRecord createMatchRecord(String data, String orientation){
//        String[] parts = data.split(",");
//        if(parts.length != 11)
//            return null;
//
//        MatchRecord newMatchRecord = new MatchRecord();
//
//        try {
//            newMatchRecord.put("Color", (parts[0].equals("B") ? 1 : 2));
//            newMatchRecord.put("Match Number", Integer.parseInt(parts[1]));
//            newMatchRecord.put("Team Number", Integer.parseInt(parts[2]));
//            for(int i = 1; i < 9; i++) {
//                newMatchRecord.put("Barrier " + i, Integer.parseInt(parts[i + 2]));
//            }
//
//            if(orientation.equals("blue-on-left")) {
//                newMatchRecord.put("Orientation", 1);
//            } else {
//                newMatchRecord.put("Orientation", 2);
//            }
//
//            return newMatchRecord;
//        } catch (NumberFormatException e) {
//            return null;
//        }
//    }
