package com.example.oleg.restaurants.network;

import android.os.AsyncTask;

import com.example.oleg.restaurants.stores.QueryStores;

import java.io.IOException;
import java.util.Map;

/**
 * Created by oleg on 25.10.17.
 */

public class DownloadTask extends AsyncTask<String, Void, DownloadTask.Result > {

    private DownloadTaskCallback<Result> mCallback;
    private Map<String, String> queryParameters;
    private String urlString;


    DownloadTask(DownloadTaskCallback<Result> callback, Map<String, String> queryParameters) {
        mCallback = callback;
        this.queryParameters = queryParameters;
    }

    @Override
    protected DownloadTask.Result doInBackground(String... urls) {
        Result result = null;
        if (!isCancelled() && urls != null && urls.length > 0) {
            urlString = urls[0];
            String query = urlString;
            if (queryParameters != null) {
                query += queryParameters.toString();
            }
            String resultString = QueryStores.initInstance().getQueryResult(query);
            if (resultString != null) {
                return new Result(resultString);
            }
            try {
                HttpRequest request = new HttpRequest(urlString, queryParameters);
                resultString = request.makeRequest();
                if (resultString != null) {
                    result = new Result(resultString);
                } else {
                    throw new IOException("Error Response");
                }
            } catch(Exception e) {
                result = new Result(e);
            }
        }
        return result;
    }


    @Override
    protected void onPostExecute(Result result) {
        if (result.mResultValue != null) {
            String query = urlString;
            if (queryParameters!= null) {
                query += queryParameters.toString();
            }
            QueryStores.initInstance().addQueryResult(query, result.mResultValue);
        }
        if (mCallback != null) {
            mCallback.finishDownloading(result);
        }
    }

    public static class Result {
        String mResultValue;
        Exception mException;
        Result(String resultValue) {
            mResultValue = resultValue;
        }
        Result(Exception exception) {
            mException = exception;
        }
    }
}
