package com.example.oleg.restaurants.models;


/**
 * Created by oleg on 15.11.17.
 */

public class Restaurant {
    private int id;
    private String  thumb;
    private String name;
    private Rating user_rating;
    private String url;
    private Location location;
    private int average_cost_for_two;
    private int price_range;
    private String menu_url;
    private String photos_url;

    @Override
    public String toString() {
        return thumb;
    }

    public String getThumb() { return thumb; }
    public Rating getRating() { return user_rating; }
    public String getName() { return name; }
    public int getId() { return id; }

    public String getMenu_url() {
        return menu_url;
    }

    public String getPhotos_url() {
        return photos_url;
    }

    public Location getLocation() {
        return location;
    }

    public int getPrice_range() {
        return price_range;
    }

    public int getAverage_cost_for_two() {
        return average_cost_for_two;
    }

    public String getUrl() {
        return url;
    }
}
