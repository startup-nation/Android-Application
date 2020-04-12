package com.example.bookmymeal.Backend;

public class RestaurantRequestInfo {

    String Restaurant;
    String isVerified;
    String comment;
    String registrationDateTime;

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRegistrationDateTime(String registrationDateTime) {
        this.registrationDateTime = registrationDateTime;
    }

    public String getComment() {
        return comment;
    }

    public String getRegistrationDateTime() {
        return registrationDateTime;
    }

    public String getRestaurant() {
        return Restaurant;
    }


    public String getIsVerified() {
        return isVerified;
    }

    public void setRestaurant(String restaurant) {
        Restaurant = restaurant;
    }


    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }
}
