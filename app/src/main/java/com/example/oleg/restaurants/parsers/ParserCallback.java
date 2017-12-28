package com.example.oleg.restaurants.parsers;

/**
 * Created by oleg on 28.10.17.
 */

public interface ParserCallback<T> {
    void finishParse(T result);
}
