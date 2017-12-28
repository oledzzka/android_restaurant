package com.example.oleg.restaurants.models;

/**
 * Created by oleg on 15.11.17.
 */

public class Location {
    String address;// "1 5th Avenue, New York, NY 10003",
    String locality; //"Greenwich Village",
    String city; //"New York City",
    Double latitude; //"40.732013",
    Double longitude; //"-73.996155",
    int country_id; //"216"

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getLocality() {
        return locality;
    }
}
