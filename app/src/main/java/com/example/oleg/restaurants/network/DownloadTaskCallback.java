package com.example.oleg.restaurants.network;


/**
 * Created by oleg on 25.10.17.
 */

public interface DownloadTaskCallback<T> {
    void finishDownloading(T result);
}
