package com.mindef.gob.models;

public class Answer {
    private int idAnswer;
    private String subjectAnswer;
    private String documentAnswer;

    public Answer() {
    }

    public Answer(int idAnswer, String subjectAnswer, String documentAnswer) {
        this.idAnswer = idAnswer;
        this.subjectAnswer = subjectAnswer;
        this.documentAnswer = documentAnswer;
    }

    public int getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(int idAnswer) {
        this.idAnswer = idAnswer;
    }

    public String getSubjectAnswer() {
        return subjectAnswer;
    }

    public void setSubjectAnswer(String subjectAnswer) {
        this.subjectAnswer = subjectAnswer;
    }

    public String getDocumentAnswer() {
        return documentAnswer;
    }

    public void setDocumentAnswer(String documentAnswer) {
        this.documentAnswer = documentAnswer;
    }
}
