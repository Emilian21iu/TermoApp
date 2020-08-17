package com.example.starnet.termoapp;

public class UserProfile {
    public String Age;
    public String Email;
    public String Name;


    public UserProfile(String Age, String Email, String Name) {
        this.Age = Age;
        this.Email = Email;
        this.Name = Name;
    }

    public String getUserAge() {
        return Age;
    }

    public void setUserAge(String Age) {
        this.Age = Age;
    }

    public String getUserEmail() {
        return Email;
    }

    public void setUserEmail(String Email) {
        this.Email = Email;
    }

    public String getUserName() {
        return Name;
    }

    public void setUserName(String Name) {
        this.Name = Name;
    }
}