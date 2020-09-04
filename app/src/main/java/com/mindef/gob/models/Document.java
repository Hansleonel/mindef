package com.mindef.gob.models;

public class Document {
    private int idDocument;
    private String codeDocument;
    private String typeDocument;
    private String subjectDocument;
    private String dateDocument;

    public Document() {
    }

    public Document(int idDocument, String codeDocument, String typeDocument, String subjectDocument, String dateDocument) {
        this.idDocument = idDocument;
        this.codeDocument = codeDocument;
        this.typeDocument = typeDocument;
        this.subjectDocument = subjectDocument;
        this.dateDocument = dateDocument;
    }

    public int getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(int idDocument) {
        this.idDocument = idDocument;
    }

    public String getCodeDocument() {
        return codeDocument;
    }

    public void setCodeDocument(String codeDocument) {
        this.codeDocument = codeDocument;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
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
