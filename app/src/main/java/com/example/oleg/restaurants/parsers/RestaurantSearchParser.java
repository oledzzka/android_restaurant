package com.example.oleg.restaurants.parsers;


import com.example.oleg.restaurants.models.Restaurant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleg on 15.11.17.
 */

public class RestaurantSearchParser implements Parser {

    private class JSONSearchResponse {
        List<JSONRestaurant> restaurants;
    }

    private class JSONRestaurant {
        Restaurant restaurant;
    }

    private ParserCallback<List<Restaurant>> mCallback;
    private Thread threadParse;

    public RestaurantSearchParser(ParserCallback<List<Restaurant>> mCallback) { this.mCallback = mCallback; }

    @Override
    public void parse(final String jsonString) {
        threadParse = new Thread(new Runnable() {
            @Override
            public void run() {
                if ( jsonString != null ) {
                    Gson gson = new Gson();
                    JSONSearchResponse response = gson.fromJson(jsonString, JSONSearchResponse.class);
                    ArrayList<Restaurant> restaurantArrayList= new ArrayList<>();
                    for (JSONRestaurant jsonRestaurant : response.restaurants) {
                        restaurantArrayList.add(jsonRestaurant.restaurant);
                    }
                    mCallback.finishParse(restaurantArrayList);
                } else {
                    mCallback.finishParse(null);
                }

            }
        });
        threadParse.start();
    }

    @Override
    public void stopParse() {
        if (threadParse != null && !threadParse.isInterrupted() ) { threadParse.interrupt(); }
    }
}

