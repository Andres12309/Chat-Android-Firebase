package com.studentsac.iachat.models;

public class Story {
    private String id;
    private String idUser;
    private String idea;
    private String url;
    private String json;
    private long timestamp;
    private long timestampLimit;

    public Story(){

    }

    public Story(String id, String idUser, String idea, String url, String json, long timestamp, long timestampLimit) {
        this.id = id;
        this.idUser = idUser;
        this.idea = idea;
        this.url = url;
        this.json = json;
        this.timestamp = timestamp;
        this.timestampLimit = timestampLimit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestampLimit() {
        return timestampLimit;
    }

    public void setTimestampLimit(long timestampLimit) {
        this.timestampLimit = timestampLimit;
    }
}
