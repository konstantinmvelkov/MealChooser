package com.mealChooser.v1;

import android.media.Image;
import android.widget.ImageButton;

public class Meals {
    //private ImageButton btn;
    private int _id;
    private String mealPicturePath;
    private String mealName;
    private String mealDescription;
    private String mealRecipe;



    public Meals(String p,String n,String d,String r) {
        this.mealPicturePath = p;
        this.mealName = n;
        this.mealDescription = d;
        this.mealRecipe = r;
    }


    public String getName() {
        return mealName;
    }

    public void setName(String name) {
        this.mealName = name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getPath() {

        return mealPicturePath;
    }

    public void setPath(String path) {
        this.mealPicturePath = path;
    }

    public String getMealDescription() {
        return mealDescription;
    }

    public void setMealDescription(String mealDescription) {
        this.mealDescription = mealDescription;
    }

    public String getMealRecipe() {
        return mealRecipe;
    }

    public void setMealRecipe(String mealRecipe) {
        this.mealRecipe = mealRecipe;
    }

}
