package com.example.oleg.restaurants.models;

import java.io.Serializable;

/**
 * Created by oleg on 27.10.17.
 */

public class Category implements Serializable {

    private String name;
    private int id;


    public String getName(){ return name; }
    public int getId(){ return id; }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
