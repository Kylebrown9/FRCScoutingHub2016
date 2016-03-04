package org.ncfrcteams.frcscoutinghub2016.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Admin on 2/26/2016.
 */
public class MatchRecord<K,V> implements Serializable {
    private final int TeamNum;
    private final int MatchNum;
    private Map<K,V> map;
    private List<Pair<K,V>> pairList;

    public MatchRecord(int TeamNum, int MatchNum) {
        this.TeamNum = TeamNum;
        this.MatchNum = MatchNum;
        map = new TreeMap<>();
        pairList = new ArrayList<>();
    }

    public MatchRecord(int TeamNum, int MatchNum, Map<K,V> map) {
        this.TeamNum = TeamNum;
        this.MatchNum = MatchNum;
        this.map = map;
    }

    public boolean equals(Object o) {
        if(o.getClass() != this.getClass())
            return false;

        MatchRecord other = (MatchRecord) o;
        return this.TeamNum == other.TeamNum && this.MatchNum == other.MatchNum;
    }

    public synchronized void put(K key, V value) {
        pairList.add(new Pair<>(key,map.get(key)));
        map.put(key,value);
    }

    public synchronized void undo() {
        int last = pairList.size()-1;
        Pair<K,V> lastState = pairList.get(last);
        pairList.remove(last);
        map.put(lastState.key,lastState.value);
    }

    public synchronized V get(K key) {
        return map.get(key);
    }

    public synchronized MatchRecord getCleanClone() {
        return new MatchRecord<>(TeamNum,MatchNum,new HashMap<>(map));
    }

    public int getMatchNum() {
        return MatchNum;
    }

    public int getTeamNum() {
        return TeamNum;
    }

    public class Pair<K1,V1> {
        public K1 key;
        public V1 value;

        public Pair(K1 key, V1 value) {
            this.key = key;
            this.value = value;
        }
    }
}
