package com.studentsac.iachat.models;

public class Message {

    private String id;
    private String idUserSend;
    private String idUserReceive;
    private String idChat;
    private String message;
    private String status;
    private Long timestamp;

    public Message(){

    }

    public Message(String id, String idUserSend, String idUserReceive, String idChat, String message, String status, Long timestamp) {
        this.id = id;
        this.idUserSend = idUserSend;
        this.idUserReceive = idUserReceive;
        this.idChat = idChat;
        this.message = message;
        this.status = status;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
