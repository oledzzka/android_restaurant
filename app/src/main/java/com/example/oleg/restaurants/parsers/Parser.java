package com.example.oleg.restaurants.parsers;

/**
 * Created by oleg on 28.10.17.
 */

public interface Parser {
    void parse(String jsonString);
    void stopParse();
}
