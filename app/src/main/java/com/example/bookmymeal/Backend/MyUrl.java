package com.example.bookmymeal.Backend;

import androidx.appcompat.widget.AppCompatSpinner;

public class MyUrl {

    public static final String Host="https://bookmymealapi.herokuapp.com";
    public static final String ApiUrl=Host+"/api";
    public static final String UserUrl=ApiUrl+"/User";
    public static final String UserTypeUrl=UserUrl+"/Customer";
    public static final String UserRegistrationUrl=UserTypeUrl+"/Registration";

    public static final String UserLogin=UserUrl+"/Login";
    public static final String UserProfile=ApiUrl+"/Profile";
    public static final String ProfileUpdate=UserProfile+"/Update";

    public static final String RestaurantRequestList=ApiUrl+"/Admin/Admin/GetAllRestaurentRequest";
    public static final String RestaurantRegistration=UserUrl+"/Restaurent/Registration";

    public static final String FAQ= ApiUrl+"/FAQ";
    public static final String EditFAQ=FAQ+"/";
}
