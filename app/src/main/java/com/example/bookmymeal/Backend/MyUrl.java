package com.example.bookmymeal.Backend;

public class MyUrl {

    public static final String Host="https://bookmymealapi.herokuapp.com";
    public static final String ApiUrl=Host+"/api";
    public static final String UserUrl=ApiUrl+"/User";
    public static final String UserTypeUrl=UserUrl+"/Customer";
    public static final String UserRegistrationUrl=UserTypeUrl+"/Registration";

    public static final String UserLogin=UserUrl+"/Login";
}
