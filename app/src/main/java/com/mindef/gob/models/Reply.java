package com.mindef.gob.models;

public class Reply {
    private String codeReply;
    private String dateReply;
    private String serieReply;
    private String subjectReply;
    private String sourceReply;
    private int idFileReply;

    public Reply() {
    }

    public Reply(String codeReply, String dateReply, String serieReply, String subjectReply, String sourceReply, int idFileReply) {
        this.codeReply = codeReply;
        this.dateReply = dateReply;
        this.serieReply = serieReply;
        this.subjectReply = subjectReply;
        this.idFileReply = idFileReply;
        this.sourceReply = sourceReply;
    }

    public String getCodeReply() {
        return codeReply;
    }

    public void setCodeReply(String codeReply) {
        this.codeReply = codeReply;
    }

    public String getDateReply() {
        return dateReply;
    }

    public void setDateReply(String dateReply) {
        this.dateReply = dateReply;
    }

    public String getSerieReply() {
        return serieReply;
    }

    public void setSerieReply(String serieReply) {
        this.serieReply = serieReply;
    }

    public String getSubjectReply() {
        return subjectReply;
    }

    public void setSubjectReply(String subjectReply) {
        this.subjectReply = subjectReply;
    }

    public String getSourceReply() {
        return sourceReply;
    }

    public void setSourceReply(String sourceReply) {
        this.sourceReply = sourceReply;
    }

    public int getIdFileReply() {
        return idFileReply;
    }

    public void setIdFileReply(int idFileReply) {
        this.idFileReply = idFileReply;
    }
}
