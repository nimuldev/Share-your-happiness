package com.share.your.happiness.shareyourhappiness.Modle_Class;

public class MyFood {
    private String area;
    private String date;
    private String distirct;
    private String division;
    private String name;
    private String phone;
    private String FoodPicture;
    private String post;
    private String foodId;
    private String upazila;
    private String userId;
    private String frizzing;
    private String description;
    private String firstName;
    private String profilePicture;
    private String available;
    private String house;
    private String time;
    private String totalReceive;
    private String totalDelivery;


    public MyFood() {

    }

    public MyFood(String area, String date, String distirct, String division, String name, String phone, String foodPicture, String post, String foodId, String upazila, String userId, String frizzing, String description, String firstName, String profilePicture, String available, String house, String time, String totalReceive, String totalDelivery) {
        this.area = area;
        this.date = date;
        this.distirct = distirct;
        this.division = division;
        this.name = name;
        this.phone = phone;
        FoodPicture = foodPicture;
        this.post = post;
        this.foodId = foodId;
        this.upazila = upazila;
        this.userId = userId;
        this.frizzing = frizzing;
        this.description = description;
        this.firstName = firstName;
        this.profilePicture = profilePicture;
        this.available = available;
        this.house = house;
        this.time = time;
        this.totalReceive = totalReceive;
        this.totalDelivery = totalDelivery;
    }

    public String getTotalReceive() {
        return totalReceive;
    }

    public String getTotalDelivery() {
        return totalDelivery;
    }

    public String getTime() {
        return time;
    }

    public String getHouse() {
        return house;
    }

    public String getArea() {
        return area;
    }

    public String getDate() {
        return date;
    }

    public String getDistirct() {
        return distirct;
    }

    public String getDivision() {
        return division;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getFoodPicture() {
        return FoodPicture;
    }

    public String getPost() {
        return post;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getUpazila() {
        return upazila;
    }

    public String getUserId() {
        return userId;
    }

    public String getFrizzing() {
        return frizzing;
    }

    public String getDescription() {
        return description;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getAvailable() {
        return available;
    }
}
