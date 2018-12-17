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

import com.Advanced.Academy.Mahfazty.models.Transaction;
import com.Advanced.Academy.Mahfazty.models.Type;

import java.util.ArrayList;


public class TransactionsDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Money.db";
    public static final String EXPENSES_TABLE_NAME = "expenses";
    public static final String INCOME_TABLE_NAME = "income";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_TYPE = "type";


    public TransactionsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Helper.createAllTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EXPENSES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + INCOME_TABLE_NAME);
        onCreate(db);
    }

    //method that inserts transaction to the needed database that depends on type
    public boolean insertTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AMOUNT, transaction.getAmount());
        contentValues.put(COLUMN_NOTE, transaction.getNote());
        contentValues.put(COLUMN_DATE, transaction.getDate());
        contentValues.put(COLUMN_CATEGORY, transaction.getCategory());
        contentValues.put(COLUMN_TYPE, transaction.getType().getValue());
        db.insert(transaction.getType() == Type.EXPENSE ? EXPENSES_TABLE_NAME : INCOME_TABLE_NAME, null, contentValues);
        return true;
    }

    public void deleteTransaction(Type type, Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(type == Type.EXPENSE ? EXPENSES_TABLE_NAME : INCOME_TABLE_NAME, "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public boolean updateTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AMOUNT, transaction.getAmount());
        contentValues.put(COLUMN_NOTE, transaction.getNote());
        contentValues.put(COLUMN_DATE, transaction.getDate());
        contentValues.put(COLUMN_CATEGORY, transaction.getCategory());
        contentValues.put(COLUMN_TYPE, transaction.getType().getValue());
        db.update(transaction.getType() == Type.EXPENSE ? EXPENSES_TABLE_NAME : INCOME_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(transaction.getId())});
        return true;
    }

    public Transaction getTransaction(Type type, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + (type == Type.EXPENSE ? EXPENSES_TABLE_NAME : INCOME_TABLE_NAME) + " where id=" + id + "", null);
        res.moveToFirst();
        Transaction transaction = new Transaction();
        transaction.setId(res.getInt(res.getColumnIndex("id")));
        transaction.setAmount(res.getLong(res.getColumnIndex(COLUMN_AMOUNT)));
        transaction.setNote(res.getString(res.getColumnIndex(COLUMN_NOTE)));
        transaction.setDate(res.getLong(res.getColumnIndex(COLUMN_DATE)));
        transaction.setCategory(res.getString(res.getColumnIndex(COLUMN_CATEGORY)));
        transaction.setType(type);
        res.close();
        return transaction;
    }

    //next 2 methods get all expenses or incomes as list of Transaction


    public ArrayList<Transaction> getAllExpenses() {
        ArrayList<Transaction> array_list = new ArrayList<>();

        //open database and make query to select all data
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + EXPENSES_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            //create model and add it to list
            Transaction transaction = new Transaction();
            transaction.setId(res.getInt(res.getColumnIndex("id")));
            transaction.setAmount(res.getLong(res.getColumnIndex(COLUMN_AMOUNT)));
            transaction.setNote(res.getString(res.getColumnIndex(COLUMN_NOTE)));
            transaction.setDate(res.getLong(res.getColumnIndex(COLUMN_DATE)));
            transaction.setCategory(res.getString(res.getColumnIndex(COLUMN_CATEGORY)));
            transaction.setType(Type.EXPENSE);
            array_list.add(transaction);
            res.moveToNext();
        }
        //close database to prevent memory leaks.
        res.close();
        return array_list;
    }

    public ArrayList<Transaction> getAllIncomes() {
        ArrayList<Transaction> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + INCOME_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Transaction transaction = new Transaction();
            transaction.setId(res.getInt(res.getColumnIndex("id")));
            transaction.setAmount(res.getLong(res.getColumnIndex(COLUMN_AMOUNT)));
            transaction.setNote(res.getString(res.getColumnIndex(COLUMN_NOTE)));
            transaction.setDate(res.getLong(res.getColumnIndex(COLUMN_DATE)));
            transaction.setCategory(res.getString(res.getColumnIndex(COLUMN_CATEGORY)));
            transaction.setType(Type.INCOME);
            array_list.add(transaction);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
}