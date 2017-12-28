package com.example.oleg.restaurants.network;

import com.example.oleg.restaurants.models.City;
import com.example.oleg.restaurants.models.Restaurant;
import com.example.oleg.restaurants.parsers.ParserCallback;
import com.example.oleg.restaurants.parsers.RestaurantSearchParser;

import java.util.HashMap;
import java.util.List;

/**
 * Created by oleg on 15.11.17.
 */

public class RestaurantSearcher implements DownloadTaskCallback<DownloadTask.Result>,
        ParserCallback<List<Restaurant>>{

    private DownloadTask downloadTask;
    private DownloadTaskCallback<Result> callback;
    private RestaurantSearchParser parser;

    public RestaurantSearcher(DownloadTaskCallback<Result> callback) {
        this.callback = callback;
    }

    public void startDowanload(Integer categoryId, City city) {
        if (city == null) {
            finishParse(null);
            return;
        }
        HashMap<String, String> query = new HashMap<>();
        query.put("category", categoryId.toString());
        query.put("sort", "rating");
        query.put("order", "desc");
        query.put("entity_type", "city");
        query.put("entity_id", String.valueOf(city.getId()));
        query.put("count", "20");
        downloadTask = new DownloadTask(this, query);
        downloadTask.execute(Constants.searchRestaurant);
    }

    public void stopDownload() {
        if (downloadTask != null ) { downloadTask.cancel(true); }
        if (parser != null ) { parser.stopParse(); }
    }

    @Override
    public void finishDownloading(DownloadTask.Result result) {
        if (result.mResultValue == null) {
            callback.finishDownloading(new Result(null, result.mException));
        } else {
            parser = new RestaurantSearchParser(this);
            parser.parse(result.mResultValue);
        }
    }

    @Override
    public void finishParse(List<Restaurant> result) {
        if (result == null) {
            callback.finishDownloading(new Result(null,
                    new RuntimeException("Error parse restaurant search")));
        } else {
            callback.finishDownloading(new Result(result, null));
        }
    }

    public static class Result{
        public List<Restaurant> restaurants;
        public Exception exception;

        Result(List<Restaurant> categories, Exception exception) {
            this.restaurants = categories;
            this.exception = exception;
        }
    }
}
