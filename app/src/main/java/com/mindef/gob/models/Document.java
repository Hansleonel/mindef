package com.mindef.gob.models;

public class Document {
    private int idDocument;
    private String typeDocument;
    private String titleDocument;
    private String subjectDocument;
    private String dateDocument;

    public Document() {
    }

    public Document(int idDocument, String typeDocument, String titleDocument, String subjectDocument, String dateDocument) {
        this.idDocument = idDocument;
        this.typeDocument = typeDocument;
        this.titleDocument = titleDocument;
        this.subjectDocument = subjectDocument;
        this.dateDocument = dateDocument;
    }

    public int getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(int idDocument) {
        this.idDocument = idDocument;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    public String getTitleDocument() {
        return titleDocument;
    }

    public void setTitleDocument(String titleDocument) {
        this.titleDocument = titleDocument;
    }

    public String getSubjectDocument() {
        return subjectDocument;
    }

    public void setSubjectDocument(String subjectDocument) {
        this.subjectDocument = subjectDocument;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }
}
