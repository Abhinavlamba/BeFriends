package com.facebooklogin.application;

import com.google.gson.annotations.SerializedName;

public class PhoneApi {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private boolean status;
    @SerializedName("user_id")
    private int UserId;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}
    public boolean getStatus() {return status;}
    public void setStatus(boolean status) {this.status = status;}

}
