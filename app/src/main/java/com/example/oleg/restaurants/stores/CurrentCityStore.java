package com.example.oleg.restaurants.stores;

import com.example.oleg.restaurants.models.City;

/**
 * Created by oleg on 06.12.17.
 */

public class CurrentCityStore {
    private static CurrentCityStore instance;
    private City city;

    private CurrentCityStore(){
    }

    public static CurrentCityStore initInstance() {
        if (instance == null ) {
            synchronized (StoreCategory.class) {
                if (instance == null ) {
                    instance = new CurrentCityStore();
                }
            }
        }
        return instance;
    }

    public City getCity() { return city; }

    public void setCity(City city) { this.city= city; }


}
