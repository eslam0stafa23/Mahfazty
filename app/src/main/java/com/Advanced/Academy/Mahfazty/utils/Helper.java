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

import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;

import java.util.Locale;

import static com.Advanced.Academy.Mahfazty.utils.CategoriesDBHelper.CATEGORIES_COLUMN_NAME;
import static com.Advanced.Academy.Mahfazty.utils.CategoriesDBHelper.CATEGORIES_COLUMN_PARENT;
import static com.Advanced.Academy.Mahfazty.utils.CategoriesDBHelper.CATEGORIES_COLUMN_TYPE;
import static com.Advanced.Academy.Mahfazty.utils.CategoriesDBHelper.CATEGORIES_TABLE_NAME;
import static com.Advanced.Academy.Mahfazty.utils.TransactionsDBHelper.COLUMN_AMOUNT;
import static com.Advanced.Academy.Mahfazty.utils.TransactionsDBHelper.COLUMN_CATEGORY;
import static com.Advanced.Academy.Mahfazty.utils.TransactionsDBHelper.COLUMN_DATE;
import static com.Advanced.Academy.Mahfazty.utils.TransactionsDBHelper.COLUMN_NOTE;
import static com.Advanced.Academy.Mahfazty.utils.TransactionsDBHelper.COLUMN_TYPE;
import static com.Advanced.Academy.Mahfazty.utils.TransactionsDBHelper.EXPENSES_TABLE_NAME;
import static com.Advanced.Academy.Mahfazty.utils.TransactionsDBHelper.INCOME_TABLE_NAME;


public class Helper {

    public static void setLocale(Context baseContext, Context context) {
        //setting needed language before setting content view
        String languageToLoad = context.getSharedPreferences("money", 0).getString("locale", "en"); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        baseContext.getResources().updateConfiguration(config, baseContext.getResources().getDisplayMetrics());
    }

    public static void createAllTables(SQLiteDatabase db) {
        //create all 3 tables at once
        db.execSQL(
                "create table if not exists " + CATEGORIES_TABLE_NAME +
                        " (id integer primary key, "
                        + CATEGORIES_COLUMN_NAME + " text, "
                        + CATEGORIES_COLUMN_PARENT + " text, "
                        + CATEGORIES_COLUMN_TYPE + " text)"

        );
        db.execSQL(
                "create table if not exists " + EXPENSES_TABLE_NAME +
                        " (id integer primary key, "
                        + COLUMN_AMOUNT + " integer, "
                        + COLUMN_NOTE + " text, "
                        + COLUMN_DATE + " integer, "
                        + COLUMN_CATEGORY + " text, "
                        + COLUMN_TYPE + " text)"

        );
        db.execSQL(
                "create table if not exists " + INCOME_TABLE_NAME +
                        " (id integer primary key, "
                        + COLUMN_AMOUNT + " integer, "
                        + COLUMN_NOTE + " text, "
                        + COLUMN_DATE + " integer, "
                        + COLUMN_CATEGORY + " text, "
                        + COLUMN_TYPE + " text)"

        );
    }
}
