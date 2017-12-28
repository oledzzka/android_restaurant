package com.example.oleg.restaurants.network;


/**
 * Created by oleg on 28.10.17.
 */

public interface DownloadCategoriesCallback {
    void finishDownloadCategories(CategoriesDownloader.Result result);
}
