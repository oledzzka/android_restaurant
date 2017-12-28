package com.example.oleg.restaurants.network;

import com.example.oleg.restaurants.models.Category;
import com.example.oleg.restaurants.parsers.CategoriesParser;
import com.example.oleg.restaurants.parsers.ParserCallback;

import java.util.List;

import static com.example.oleg.restaurants.network.Constants.urlCategories;

/**
 * Created by oleg on 28.10.17.
 */

public class CategoriesDownloader implements DownloadTaskCallback<DownloadTask.Result>,
        ParserCallback<List<Category>>{
    private DownloadCategoriesCallback categoriesCallback;
    private DownloadTask downloadTask;
    private CategoriesParser parser;

    public CategoriesDownloader(DownloadCategoriesCallback categoriesCallback) {
        this.categoriesCallback = categoriesCallback;
    }

    public class Result{
        public List<Category> categories;
        public Exception exception;

        Result(List<Category> categories, Exception exception) {
            this.categories = categories;
            this.exception = exception;
        }
    }

    public void startDownload() {
        downloadTask = new DownloadTask(this, null);
        downloadTask.execute(urlCategories);
    }

    public void stopDownload() {
        if (downloadTask != null ) { downloadTask.cancel(true); }
        if (parser != null ) { parser.stopParse(); }
    }

    @Override
    public void finishParse(List<Category> result) {
        if (result == null) {
            categoriesCallback.finishDownloadCategories(new Result(null, new RuntimeException("Error response")));
        } else {
            categoriesCallback.finishDownloadCategories(new Result(result, null));
        }
    }


    @Override
    public void finishDownloading(DownloadTask.Result result) {
        if (result.mResultValue == null) {
            categoriesCallback.finishDownloadCategories(new Result(null, result.mException));

        } else {
            parser = new CategoriesParser(this);
            parser.parse(result.mResultValue);
        }
    }
}
