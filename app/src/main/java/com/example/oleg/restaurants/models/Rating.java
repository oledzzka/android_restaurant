package com.example.oleg.restaurants.models;

/**
 * Created by oleg on 15.11.17.
 */

public class Rating {
    Float aggregate_rating;
    String rating_text;
    String rating_color;
    int votes;


    public Float getAggregate_rating() { return aggregate_rating; }

    public String getRating_color() {
        return rating_color;
    }

    public int getVotes() {
        return votes;
    }
}
