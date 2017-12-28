package com.example.oleg.restaurants.stores;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by oleg on 15.11.17.
 */

public class ImageStores {
    private static ImageStores instance;
    private LruCache<String, Bitmap> mImageCache;

    private ImageStores(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mImageCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static ImageStores initInstance() {
        if (instance == null ) {
            synchronized (StoreCategory.class) {
                if (instance == null ) {
                    instance = new ImageStores();
                }
            }
        }
        return instance;
    }

    public synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mImageCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mImageCache.get(key);
    }
}
