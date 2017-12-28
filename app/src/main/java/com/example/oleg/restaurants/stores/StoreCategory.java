package com.example.oleg.restaurants.stores;

import com.example.oleg.restaurants.models.Category;

import java.util.List;

/**
 * Created by oleg on 28.10.17.
 */

public class StoreCategory {
    private static StoreCategory instance;
    private List<Category> categories;

    private StoreCategory(){
    }

    public static StoreCategory initInstance() {
        if (instance == null ) {
            synchronized (StoreCategory.class) {
                if (instance == null ) {
                    instance = new StoreCategory();
                }
            }
        }
        return instance;
    }

    public List<Category> getCategories() { return categories; }

    public void setCategories(List<Category> categories) { this.categories = categories; }

}
