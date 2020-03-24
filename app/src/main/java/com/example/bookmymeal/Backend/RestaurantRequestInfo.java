package com.example.bookmymeal.Backend;

public class RestaurantRequestInfo {

    String RestaurantName;
    String RestaurantImage;
    String isVerified;

    public String getRestaurantName() {
        return RestaurantName;
    }

    public String getRestaurantImage() {
        return RestaurantImage;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setRestaurantName(String restaurantName) {
        RestaurantName = restaurantName;
    }

    public void setRestaurantImage(String restaurantImage) {
        RestaurantImage = restaurantImage;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }
}
