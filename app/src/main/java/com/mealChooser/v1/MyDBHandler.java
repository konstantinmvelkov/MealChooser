package com.mealChooser.v1;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import java.util.*;
public class MyDBHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mealsSelected.db";

    public static final String TABLE_SHOW_HELP = "help";
    public static final String COLUMN_SHOW_HELP ="showHelp";
    public static final String COLUMN_ID_HELP = "_id_Help";

    public static final String TABLE_PRODUCTS = "meals";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MEALPIC ="mealPic";
    public static final String COLUMN_MEALNAME ="mealName";
    public static final String COLUMN_MEALDESCRIPTION ="mealDescription";
    public static final String COLUMN_MEALRECIPE ="mealRecipe";

    public MyDBHandler(Context context) {
        super(context, null, null, 1);
    }
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,DATABASE_NAME,factory,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableMeals = "CREATE TABLE "+TABLE_PRODUCTS+ "(" + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_MEALPIC +"  TEXT," + COLUMN_MEALNAME + "  TEXT,"+
                COLUMN_MEALDESCRIPTION + "  TEXT ,"+ COLUMN_MEALRECIPE + "  TEXT "+");";
        String createTableHelp = "CREATE TABLE "+TABLE_SHOW_HELP+ "(" + COLUMN_ID_HELP +" INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_SHOW_HELP +"  TEXT "+");";
        db.execSQL(createTableMeals);
        db.execSQL(createTableHelp);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PRODUCTS);
        onCreate(db);
    }

    public void deleteProduct(String mealName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_PRODUCTS+" WHERE "+COLUMN_MEALNAME+"=\""+ mealName+"\";");
    }
    public void updateProduct(String mealName,String newDescription, String newRecipe) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_PRODUCTS+" SET "+ COLUMN_MEALDESCRIPTION+ "=\""+ newDescription+ "\"" + " WHERE "+COLUMN_MEALNAME+"=\""+ mealName+"\";");
        db.execSQL("UPDATE "+TABLE_PRODUCTS+" SET "+ COLUMN_MEALRECIPE+ "=\""+ newRecipe+ "\"" + " WHERE "+COLUMN_MEALNAME+"=\""+ mealName+"\";");
    }
    public void updateShowHelp() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_SHOW_HELP+" SET "+ COLUMN_SHOW_HELP+ "=\""+ 1 + "\"" + " WHERE "+COLUMN_SHOW_HELP+"=\""+ 0 +"\";");
    }
    public void addShowHelp() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SHOW_HELP,"0");
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_SHOW_HELP,null,values);
        db.close();
    }
    public void addProduct(Meals meal) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEALPIC,meal.getPath());
        values.put(COLUMN_MEALNAME,meal.getName());
        values.put(COLUMN_MEALDESCRIPTION,meal.getMealDescription());
        values.put(COLUMN_MEALRECIPE,meal.getMealRecipe());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PRODUCTS,null,values);
        db.close();
    }
    public LinkedHashMap<Integer, List<String>> getShowHelp(){
        LinkedHashMap<Integer, List<String>> map = new LinkedHashMap<Integer, List<String>>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_SHOW_HELP+" WHERE 1";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        int i = 0;
        while(!c.isAfterLast()){
            List<String> l = new ArrayList<>();
            l.add(c.getString(c.getColumnIndex("showHelp")));
            map.put(i,l);
            i++;
            c.moveToNext();
        }
        db.close();
        return map;
    }
    public LinkedHashMap<Integer, List<String>> databaseToString(){
        LinkedHashMap<Integer, List<String>> map = new LinkedHashMap<Integer, List<String>>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_PRODUCTS+" WHERE 1";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        int i = 0;
        while(!c.isAfterLast()){
            List<String> l = new ArrayList<>();
            if(c.getString(c.getColumnIndex("mealName"))!=null) {
                if(c.getString(c.getColumnIndex("mealPic"))!=null) {
                    l.add(c.getString(c.getColumnIndex("mealName")));
                    l.add(c.getString(c.getColumnIndex("mealPic")));
                    l.add(c.getString(c.getColumnIndex("mealDescription")));
                    l.add(c.getString(c.getColumnIndex("mealRecipe")));
                    map.put(i,l);
                }
            }
            i++;
            c.moveToNext();
        }
        db.close();
        return map;
    }
}
