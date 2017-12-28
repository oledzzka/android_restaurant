package com.example.oleg.restaurants.network;

import com.example.oleg.restaurants.models.City;
import com.example.oleg.restaurants.parsers.LocationSearchParser;
import com.example.oleg.restaurants.parsers.ParserCallback;

import java.util.HashMap;
import java.util.List;

import static com.example.oleg.restaurants.network.Constants.urlSearchCity;


/**
 * Created by oleg on 06.12.17.
 */

public class CitiesDownloader implements DownloadTaskCallback<DownloadTask.Result>,
        ParserCallback<List<City>> {

    private DownloadCallbackCities callbackCities;
    private DownloadTask downloadTask;
    private LocationSearchParser parser;


    public CitiesDownloader(DownloadCallbackCities callbackCities) {
        this.callbackCities = callbackCities;
    }

    public class Result{
        public List<City> cities;
        public Exception exception;

        Result(List<City> cities, Exception exception) {
            this.cities = cities;
            this.exception = exception;
        }
    }

    public void startDownload(String query_city) {
        HashMap<String, String> query = new HashMap<>();
        query.put("q", query_city);
        query.put("count", "20");
        downloadTask = new DownloadTask(this, query);
        downloadTask.execute(urlSearchCity);
    }

    public void stopDownload() {
        if (downloadTask != null ) { downloadTask.cancel(true); }
        if (parser != null ) { parser.stopParse(); }
    }

    @Override
    public void finishDownloading(DownloadTask.Result result) {
        if (result.mResultValue == null) {
            callbackCities.finishDownloadCities(new Result(null, result.mException));

        } else {
            parser = new LocationSearchParser(this);
            parser.parse(result.mResultValue);
        }
    }

    @Override
    public void finishParse(List<City> result) {
        if (result == null) {
            callbackCities.finishDownloadCities(new Result(null, new RuntimeException("Error response")));
        } else {
            callbackCities.finishDownloadCities(new Result(result, null));
        }
    }
}
