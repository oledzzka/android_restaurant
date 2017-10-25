package com.example.oleg.instagramm;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by oleg on 25.10.17.
 */

public class DownloadTask extends AsyncTask<String, Void, DownloadTask.Result > {

    private DownloadCallback<Result> mCallback;

    DownloadTask() { mCallback = null; }

    DownloadTask(DownloadCallback<Result> callback) {
        setCallback(callback);
    }

    void setCallback(DownloadCallback<Result> callback) {
        mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        if (mCallback != null) {
            NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected() ||
                    (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                            && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                // If no connectivity, cancel task and update Callback with null data.
                Result result = new Result(new Exception("No Internet connection"));
                mCallback.updateFromDownload(result);
                cancel(true);
            }
        }
    }

    @Override
    protected DownloadTask.Result doInBackground(String... urls) {
        Result result = null;
        if (!isCancelled() && urls != null && urls.length > 0) {
            String urlString = urls[0];
            try {
                HttpRequest request = new HttpRequest(urlString);
                String resultString = request.makeRequest();
                if (resultString != null) {
                    result = new Result(resultString);
                } else {
                    throw new IOException("No response received.");
                }
            } catch(Exception e) {
                result = new Result(e);
            }
        }
        return result;
    }


    @Override
    protected void onPostExecute(Result result) {
        if (result != null && mCallback != null) {
            if (result.mException != null) {
                mCallback.updateFromDownload(result);
            } else if (result.mResultValue != null) {
                mCallback.updateFromDownload(result);
            }
            mCallback.finishDownloading();
        }
    }
    static class Result {
        public String mResultValue;
        public Exception mException;
        public Result(String resultValue) {
            mResultValue = resultValue;
        }
        public Result(Exception exception) {
            mException = exception;
        }
    }
}
