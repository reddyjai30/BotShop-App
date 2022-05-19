package com.Hackthon.botshop.Models;

public class MessagesModels {

    String uId , message;
    Long timestamp;
    String senderName , receiverName;
    String imageUrl;

    public MessagesModels(String uId, String message, Long timestamp) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
    }
    public MessagesModels(String uId, String message, Long timestamp,String imageUrl) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
    }

    public MessagesModels(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }

    public MessagesModels(){}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getuId() {
        return uId;
    }

    public MessagesModels(String uId, String message, Long timestamp, String senderName, String receiverName) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
        this.senderName = senderName;
        this.receiverName = receiverName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
