package com.example.oleg.restaurants.parsers;

import com.example.oleg.restaurants.models.City;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleg on 06.12.17.
 */

public class LocationSearchParser implements Parser {
    private ParserCallback<List<City>> mCallback;
    private Thread threadParse;

    public LocationSearchParser(ParserCallback<List<City>> callback) {
        mCallback = callback;
    }


    private class JSONLocation {
        List<City> location_suggestions;
    }

    @Override
    public void parse(final String result) {
        threadParse = new Thread(new Runnable() {
            @Override
            public void run() {
                if ( result != null ) {
                    Gson gson = new Gson();
                    LocationSearchParser.JSONLocation locations = gson.fromJson(result, LocationSearchParser.JSONLocation.class);
                    ArrayList<City> categoryArrayList = new ArrayList<>();
                    categoryArrayList.addAll(locations.location_suggestions);
                    mCallback.finishParse(categoryArrayList);
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
