package com.example.oleg.restaurants.parsers;

import com.example.oleg.restaurants.models.Category;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleg on 28.10.17.
 */

public class CategoriesParser implements Parser {

    private ParserCallback<List<Category>> mCallback;
    private Thread threadParse;

    public CategoriesParser(ParserCallback<List<Category>> callback) {
        mCallback = callback;
    }

    private class JSONCategory {
        Category categories;
    }

    private class JSONCategories {
        List<JSONCategory> categories;
    }

    @Override
    public void parse(final String result) {
        threadParse = new Thread(new Runnable() {
            @Override
            public void run() {
                if ( result != null ) {
                    Gson gson = new Gson();
                    JSONCategories categories = gson.fromJson(result, JSONCategories.class);
                    ArrayList<Category> categoryArrayList = new ArrayList<>();
                    for (JSONCategory jsonCategory : categories.categories) {
                        categoryArrayList.add(jsonCategory.categories);
                    }
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
