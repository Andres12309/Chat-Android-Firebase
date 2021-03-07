package com.studentsac.iachat.models;

import java.util.ArrayList;

public class Chat {

    private String id;
    private Long timestamp;
    private ArrayList<String> ids;

    public Chat(){

    }

    public Chat(String id, Long timestamp, ArrayList<String> ids) {
        this.id = id;
        this.timestamp = timestamp;
        this.ids = ids;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }
}
