package com.mindef.gob.models;

public class PhoneLine {
    private int idPhoneLine;
    private String namePhoneLine;
    private String imagePhoneLine;
    private String directionPhoneLine;
    private String directoryPhoneLine;

    public PhoneLine() {
    }

    public PhoneLine(int idPhoneLine, String namePhoneLine, String imagePhoneLine, String directionPhoneLine, String directoryPhoneLine) {
        this.idPhoneLine = idPhoneLine;
        this.namePhoneLine = namePhoneLine;
        this.imagePhoneLine = imagePhoneLine;
        this.directionPhoneLine = directionPhoneLine;
        this.directoryPhoneLine = directoryPhoneLine;
    }

    public int getIdPhoneLine() {
        return idPhoneLine;
    }

    public void setIdPhoneLine(int idPhoneLine) {
        this.idPhoneLine = idPhoneLine;
    }

    public String getNamePhoneLine() {
        return namePhoneLine;
    }

    public void setNamePhoneLine(String namePhoneLine) {
        this.namePhoneLine = namePhoneLine;
    }

    public String getImagePhoneLine() {
        return imagePhoneLine;
    }

    public void setImagePhoneLine(String imagePhoneLine) {
        this.imagePhoneLine = imagePhoneLine;
    }

    public String getDirectionPhoneLine() {
        return directionPhoneLine;
    }

    public void setDirectionPhoneLine(String directionPhoneLine) {
        this.directionPhoneLine = directionPhoneLine;
    }

    public String getDirectoryPhoneLine() {
        return directoryPhoneLine;
    }

    public void setDirectoryPhoneLine(String directoryPhoneLine) {
        this.directoryPhoneLine = directoryPhoneLine;
    }
}
