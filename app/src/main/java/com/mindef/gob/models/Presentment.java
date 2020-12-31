package com.mindef.gob.models;

public class Presentment {
    private int idDocPresentment;
    private String typePresentment;
    private String codePresentment;
    private String subjectPresentment;
    private String datePresentment;

    public Presentment() {
    }

    public Presentment(int idDocPresentment, String typePresentment, String codePresentment, String subjectPresentment, String datePresentment) {
        this.idDocPresentment = idDocPresentment;
        this.typePresentment = typePresentment;
        this.codePresentment = codePresentment;
        this.subjectPresentment = subjectPresentment;
        this.datePresentment = datePresentment;
    }

    public int getIdDocPresentment() {
        return idDocPresentment;
    }

    public void setIdDocPresentment(int idDocPresentment) {
        this.idDocPresentment = idDocPresentment;
    }

    public String getTypePresentment() {
        return typePresentment;
    }

    public void setTypePresentment(String typePresentment) {
        this.typePresentment = typePresentment;
    }

    public String getCodePresentment() {
        return codePresentment;
    }

    public void setCodePresentment(String codePresentment) {
        this.codePresentment = codePresentment;
    }

    public String getSubjectPresentment() {
        return subjectPresentment;
    }

    public void setSubjectPresentment(String subjectPresentment) {
        this.subjectPresentment = subjectPresentment;
    }

    public String getDatePresentment() {
        return datePresentment;
    }

    public void setDatePresentment(String datePresentment) {
        this.datePresentment = datePresentment;
    }
}
