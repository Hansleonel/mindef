package com.mindef.gob.models;

public class Tracking {
    private String officeTracking;
    private String dateTracking;
    private String noteTracking;
    private String stateTracking;
    private int positionTracking;

    public Tracking() {
    }

    public Tracking(String officeTracking, String dateTracking, String noteTracking, String stateTracking, int positionTracking) {
        this.officeTracking = officeTracking;
        this.dateTracking = dateTracking;
        this.noteTracking = noteTracking;
        this.stateTracking = stateTracking;
        this.positionTracking = positionTracking;
    }

    public String getOfficeTracking() {
        return officeTracking;
    }

    public void setOfficeTracking(String officeTracking) {
        this.officeTracking = officeTracking;
    }

    public String getDateTracking() {
        return dateTracking;
    }

    public void setDateTracking(String dateTracking) {
        this.dateTracking = dateTracking;
    }

    public String getNoteTracking() {
        return noteTracking;
    }

    public void setNoteTracking(String noteTracking) {
        this.noteTracking = noteTracking;
    }

    public String getStateTracking() {
        return stateTracking;
    }

    public void setStateTracking(String stateTracking) {
        this.stateTracking = stateTracking;
    }

    public int getPositionTracking() {
        return positionTracking;
    }

    public void setPositionTracking(int positionTracking) {
        this.positionTracking = positionTracking;
    }
}
