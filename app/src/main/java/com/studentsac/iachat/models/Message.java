package com.studentsac.iachat.models;

public class Message {

    private String id;
    private String idUserSend;
    private String idUserReceive;
    private String idChat;
    private String message;
    private String status;
    private String urlImage;
    private String type;
    private long timestamp;

    public Message(){

    }

    public Message(String id, String idUserSend, String idUserReceive, String idChat, String message, String status, String urlImage, String type, long timestamp) {
        this.id = id;
        this.idUserSend = idUserSend;
        this.idUserReceive = idUserReceive;
        this.idChat = idChat;
        this.message = message;
        this.status = status;
        this.urlImage = urlImage;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUserSend() {
        return idUserSend;
    }

    public void setIdUserSend(String idUserSend) {
        this.idUserSend = idUserSend;
    }

    public String getIdUserReceive() {
        return idUserReceive;
    }

    public void setIdUserReceive(String idUserReceive) {
        this.idUserReceive = idUserReceive;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
