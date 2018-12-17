package com.Advanced.Academy.Mahfazty.utils;

/*

* Designed and developed by
 * Eslam Mostafa Sayed
 * Amgad Mohamed Attia
 * Ashraf Mahmoud Abdulmaged
 * Amir Hussain Mostafa

as a graduation project for the year of 2017
Advanced Academy
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.Advanced.Academy.Mahfazty.models.Category;
import com.Advanced.Academy.Mahfazty.models.Type;

import java.util.ArrayList;


public class CategoriesDBHelper extends SQLiteOpenHelper {
    //Works the same as TransactionDBHelper, look there for comments

    public static final String DATABASE_NAME = "Money.db";
    public static final String CATEGORIES_TABLE_NAME = "categories";
    public static final String CATEGORIES_COLUMN_NAME = "name";
    public static final String CATEGORIES_COLUMN_PARENT = "parent";
    public static final String CATEGORIES_COLUMN_TYPE = "type";
    public static final String CATEGORIES_COLUMN_LANGUAGE = "language";


    public CategoriesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Helper.createAllTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertCategory(String language, Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORIES_COLUMN_NAME, category.getName());
        contentValues.put(CATEGORIES_COLUMN_PARENT, category.getParentCategory());
        contentValues.put(CATEGORIES_COLUMN_TYPE, category.getType().getValue());
        if (language != null) {
            contentValues.put(CATEGORIES_COLUMN_LANGUAGE, language);
        }
        db.insert(CATEGORIES_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public boolean insertCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORIES_COLUMN_NAME, category.getName());
        contentValues.put(CATEGORIES_COLUMN_PARENT, category.getParentCategory());
        contentValues.put(CATEGORIES_COLUMN_TYPE, category.getType().getValue());
        db.insert(CATEGORIES_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public void deleteCategory(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CATEGORIES_TABLE_NAME, "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public boolean updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORIES_COLUMN_NAME, category.getName());
        contentValues.put(CATEGORIES_COLUMN_PARENT, category.getParentCategory());
        contentValues.put(CATEGORIES_COLUMN_TYPE, category.getType().getValue());
        db.update(CATEGORIES_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(category.getId())});
        return true;
    }

    public Category getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CATEGORIES_TABLE_NAME + " where id=" + id + "", null);
        res.moveToFirst();
        Category category = new Category();
        category.setId(res.getInt(res.getColumnIndex("id")));
        category.setName(res.getString(res.getColumnIndex(CATEGORIES_COLUMN_NAME)));
        category.setParentCategory(res.getString(res.getColumnIndex(CATEGORIES_COLUMN_PARENT)));
        category.setType(Type.getType(res.getString(res.getColumnIndex(CATEGORIES_COLUMN_TYPE))));
        res.close();
        return category;
    }


    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CATEGORIES_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Category category = new Category();
            category.setId(res.getInt(res.getColumnIndex("id")));
            category.setName(res.getString(res.getColumnIndex(CATEGORIES_COLUMN_NAME)));
            category.setParentCategory(res.getString(res.getColumnIndex(CATEGORIES_COLUMN_PARENT)));
            category.setType(Type.getType(res.getString(res.getColumnIndex(CATEGORIES_COLUMN_TYPE))));
            array_list.add(category);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
}