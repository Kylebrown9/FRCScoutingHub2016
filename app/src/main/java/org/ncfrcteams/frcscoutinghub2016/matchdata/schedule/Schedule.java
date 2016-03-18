package org.ncfrcteams.frcscoutinghub2016.matchdata.schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kyle Brown on 3/16/2016.
 */
public class Schedule {
    private List<MatchDescriptor> matchDescriptorList;
    private List<ScheduleEntry> scheduleEntries;
    private ScheduleChangeListener scheduleChangeListener = null;

    //****************************************UI Methods*********************************

    /**
     * Creates and initializes a new Schedule object
     */
    public Schedule() {
        matchDescriptorList = new ArrayList<>();
        scheduleEntries = new ArrayList<>();
    }

    /**
     * Sets the ScheduleChangeListener for this Schedule
     * @param changeListener
     */
    public synchronized void setScheduleChangeListener(ScheduleChangeListener changeListener) {
        this.scheduleChangeListener = changeListener;
    }

    /**
     * Adds a single MatchDescriptor to the matchDescriptorList sorts it then updates the
     * scheduleEntriesList and informs the scheduleChangeListener of the changes
     * @param matchDescriptor the MatchDescriptor to be added
     */
    public synchronized void add(MatchDescriptor matchDescriptor) {
        this.matchDescriptorList.add(matchDescriptor);
        update();
    }

    /**
     * Adds a list of MatchDescriptors to the matchDescriptorList sorts it then updates the
     * scheduleEntriesList and informs the scheduleChangeListener of the changes
     * @param matchDescriptorList the list of MatchDescriptors to be added
     */
    public synchronized void addAll(List<MatchDescriptor> matchDescriptorList) {
        this.matchDescriptorList.addAll(matchDescriptorList);
        update();
    }

    /**
     * @return an up to date copy of the scheduleEntries list
     */
    public synchronized List<ScheduleEntry> getScheduleEntries() {
        return new ArrayList<>(scheduleEntries);
    }

    private void update() {
        Collections.sort(matchDescriptorList);
        scheduleEntries.clear();

        int lastNum = 1;
        int currNum;
        ScheduleEntry newEntry;
        boolean lastMatchWasQual = matchDescriptorList.get(0).isQual();

        for(MatchDescriptor matchDescriptor : matchDescriptorList) {
            currNum = matchDescriptor.getMatchNum();

            if(!matchDescriptor.isQual() && lastMatchWasQual) {
                lastNum = 1;
            }

            for(int i=lastNum; i<currNum; i++) {
                newEntry = ScheduleEntry.getBlank(i,matchDescriptor.isQual());
                scheduleEntries.add(newEntry);
            }

            newEntry = ScheduleEntry.getFromDescriptor(matchDescriptor);
            scheduleEntries.add(newEntry);

            lastNum = currNum;
            lastMatchWasQual = matchDescriptor.isQual();
        }

        if(scheduleChangeListener != null) {
            scheduleChangeListener.notifyScheduleChanges(getScheduleEntries());
        }
    }

    public static class ScheduleEntry {
        private int matchNum;
        private boolean isQual;
        private MatchDescriptor matchDescriptor;

        private ScheduleEntry(int matchNum, MatchDescriptor matchDescriptor, boolean isQual) {
            this.matchNum = matchNum;
            this.matchDescriptor = matchDescriptor;
            this.isQual = isQual;
        }

        public static ScheduleEntry getBlank(int matchNum, boolean isQual) {
            return new ScheduleEntry(matchNum,null,isQual);
        }

        public static ScheduleEntry getFromDescriptor(MatchDescriptor matchDescriptor) {
            return new ScheduleEntry(matchDescriptor.getMatchNum(),matchDescriptor,matchDescriptor.isQual());
        }

        /**
         * @return whether or not this ScheduleEntry is a placeholder or a real match
         */
        public boolean isPlaceHolder() {
            return matchDescriptor == null;
        }

        /**
         * @return a string representation of this ScheduleEntry
         */
        public String toString() {
            return (isQual? "Qual " : "Elim ") + matchNum;
        }
    }

    public interface ScheduleChangeListener {
        void notifyScheduleChanges(List<ScheduleEntry> scheduleEntries);
    }
}
