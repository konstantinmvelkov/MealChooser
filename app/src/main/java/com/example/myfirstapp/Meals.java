package com.example.myfirstapp;

import android.media.Image;
import android.widget.ImageButton;

public class Meals {
    //private ImageButton btn;
    private int _id;
    private String path;
    private String name;
    public  Meals(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Meals(String p,String n) {
        this.path = p;
        this.name = n;
    }

    public int get_id() {
        return _id;
    }

    public String getPath() {
        return path;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
