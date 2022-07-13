package com.example.demoapp.Models;

public class MessageModel {
    String uId, message, messageId,imageMess, audio;
    Long timestamp;

    public MessageModel(String uId, String message, Long timestamp ,  String imageMess) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
        this.imageMess = imageMess;
    }

    public MessageModel(String uId, String message ,String imageMess,String audio) {
        this.uId = uId;
        this.message = message;
        this.imageMess = imageMess;
        this.audio = audio;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }



//    public MessageModel(String uId, String message ,String imageMess, String checkseen) {
//        this.uId = uId;
//        this.message = message;
//        this.imageMess = imageMess;
//        this.checkSeen = checkseen;
//    }





    public MessageModel() {

    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getImageMess() {
        return imageMess;
    }

    public void setImageMess(String imageMess) {
        this.imageMess = imageMess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }



//    public String getCheckSeen() {
//        return checkSeen;
//    }
//
//    public void setCheckSeen(String checkSeen) {
//        this.checkSeen = checkSeen;
//    }
}
