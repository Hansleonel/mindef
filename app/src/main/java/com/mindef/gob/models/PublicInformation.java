package com.mindef.gob.models;

public class PublicInformation {
    private int idDocPublicInformation;
    private String codePublicInformation;
    private String subjectPublicInformation;
    private String datePublicInformation;

    public PublicInformation() {
    }

    public PublicInformation(int idDocPublicInformation, String codePublicInformation, String subjectPublicInformation, String datePublicInformation) {
        this.idDocPublicInformation = idDocPublicInformation;
        this.codePublicInformation = codePublicInformation;
        this.subjectPublicInformation = subjectPublicInformation;
        this.datePublicInformation = datePublicInformation;
    }

    public int getIdDocPublicInformation() {
        return idDocPublicInformation;
    }

    public void setIdDocPublicInformation(int idDocPublicInformation) {
        this.idDocPublicInformation = idDocPublicInformation;
    }

    public String getCodePublicInformation() {
        return codePublicInformation;
    }

    public void setCodePublicInformation(String codePublicInformation) {
        this.codePublicInformation = codePublicInformation;
    }

    public String getSubjectPublicInformation() {
        return subjectPublicInformation;
    }

    public void setSubjectPublicInformation(String subjectPublicInformation) {
        this.subjectPublicInformation = subjectPublicInformation;
    }

    public String getDatePublicInformation() {
        return datePublicInformation;
    }

    public void setDatePublicInformation(String datePublicInformation) {
        this.datePublicInformation = datePublicInformation;
    }
}
