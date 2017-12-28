package com.example.oleg.restaurants.stores;

import com.example.oleg.restaurants.models.Restaurant;

/**
 * Created by oleg on 23.12.17.
 */

public class CurrentRestaurantStore {

    private Restaurant restaurant;
    private static final CurrentRestaurantStore ourInstance = new CurrentRestaurantStore();

    public static CurrentRestaurantStore getInstance() {
        return ourInstance;
    }

    private CurrentRestaurantStore() {
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
