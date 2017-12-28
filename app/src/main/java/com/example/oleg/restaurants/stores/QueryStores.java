package com.example.oleg.restaurants.stores;

import android.support.v4.util.LruCache;

/**
 * Created by oleg on 15.11.17.
 */

public class QueryStores {
    private static QueryStores instance;
    private LruCache<String, String> mQueryCache;

    private QueryStores(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 10;
        mQueryCache = new LruCache<> (cacheSize);
    }

    public static QueryStores initInstance() {
        if (instance == null ) {
            synchronized (QueryStores.class) {
                if (instance == null ) {
                    instance = new QueryStores();
                }
            }
        }
        return instance;
    }

    public void addQueryResult(String query, String result) {
        mQueryCache.put(query, result);
    }

    public String getQueryResult(String query) {
        return mQueryCache.get(query);
    }
}
