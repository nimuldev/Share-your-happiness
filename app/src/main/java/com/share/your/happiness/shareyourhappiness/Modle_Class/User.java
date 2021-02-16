package com.share.your.happiness.shareyourhappiness.Modle_Class;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String uid;

    public User() {
    }

    public User(String email, String firstName, String lastName, String profilePicture, String uid) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getUid() {
        return uid;
    }
}
