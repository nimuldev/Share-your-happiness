package com.share.your.happiness.shareyourhappiness.Modle_Class;

public class Request {
    private String requestName;
    private String foodName;
    private String requestId;
    private String toRequestUserId;
    private String requestFoodId;
    private String requestFoodIv;
    private String fromRequestUserId;
    private String fromRequestUserIv;
    private String date;
    private String time;
    private String status;
    private String comment;
    private String ToRequestUserIv;
    private String ToRequestUserName;
    private String fromUserNumber;

    public Request() {
    }

    public Request(String requestName, String foodName, String requestId, String toRequestUserId, String requestFoodId, String requestFoodIv, String fromRequestUserId, String fromRequestUserIv, String date, String time, String status, String comment, String toRequestUserIv, String toRequestUserName, String fromUserNumber) {
        this.requestName = requestName;
        this.foodName = foodName;
        this.requestId = requestId;
        this.toRequestUserId = toRequestUserId;
        this.requestFoodId = requestFoodId;
        this.requestFoodIv = requestFoodIv;
        this.fromRequestUserId = fromRequestUserId;
        this.fromRequestUserIv = fromRequestUserIv;
        this.date = date;
        this.time = time;
        this.status = status;
        this.comment = comment;
        ToRequestUserIv = toRequestUserIv;
        ToRequestUserName = toRequestUserName;
        this.fromUserNumber = fromUserNumber;
    }

    public String getFromUserNumber() {
        return fromUserNumber;
    }

    public String getToRequestUserIv() {
        return ToRequestUserIv;
    }

    public String getToRequestUserName() {
        return ToRequestUserName;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getRequestName() {
        return requestName;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getToRequestUserId() {
        return toRequestUserId;
    }

    public String getRequestFoodId() {
        return requestFoodId;
    }

    public String getRequestFoodIv() {
        return requestFoodIv;
    }

    public String getFromRequestUserId() {
        return fromRequestUserId;
    }

    public String getFromRequestUserIv() {
        return fromRequestUserIv;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
