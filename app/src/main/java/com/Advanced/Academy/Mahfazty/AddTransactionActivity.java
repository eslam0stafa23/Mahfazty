package com.Advanced.Academy.Mahfazty;

/*

* Designed and developed by
 * Eslam Mostafa Sayed
 * Amgad Mohamed Attia
 * Ashraf Mahmoud Abdulmaged
 * Amir Hussain Mostafa

as a graduation project for the year of 2017
Advanced Academy
*/

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.Advanced.Academy.Mahfazty.models.Category;
import com.Advanced.Academy.Mahfazty.models.Transaction;
import com.Advanced.Academy.Mahfazty.models.Type;
import com.Advanced.Academy.Mahfazty.utils.CategoriesDBHelper;
import com.Advanced.Academy.Mahfazty.utils.Helper;
import com.Advanced.Academy.Mahfazty.utils.TransactionsDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddTransactionActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener {
    ImageView add;
    ImageView close;
    Spinner typeChooser;
    EditText amount;
    Spinner categoryChooser;
    EditText note;
    TextView date;

    int year = 0;
    int month;
    int dayOfMonth;
    int hourOfDay = 0;
    int minute;
    boolean isDateChosen = false;
    String selectedCategory = "";
    private TransactionsDBHelper helper;
    Transaction recevedTransaction;

    boolean needToUpdate = false;

    public static void display(Activity activity, Type type, Integer id) {
        Intent i = new Intent(activity, AddTransactionActivity.class);
        i.putExtra("type", type.getValue());
        i.putExtra("id", id);
        activity.startActivityForResult(i, 111);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.setLocale(getBaseContext(), this);
        setContentView(R.layout.activity_add_transaction);
        //initiate TransactionsDBHelper
        add = (ImageView) findViewById(R.id.add);
        close = (ImageView) findViewById(R.id.close);
        typeChooser = (Spinner) findViewById(R.id.typeSpinner);
        amount = (EditText) findViewById(R.id.amount);
        categoryChooser = (Spinner) findViewById(R.id.category);
        note = (EditText) findViewById(R.id.note);
        date = (TextView) findViewById(R.id.date);
        helper = new TransactionsDBHelper(this);
        close.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        categoryChooser.setOnItemSelectedListener(this);
        //setting up adapter with categories from the database
        CategoriesDBHelper catHelper = new CategoriesDBHelper(this);
        ArrayList<Category> categories = catHelper.getAllCategories();
        ArrayList<String> categoriesTitles = new ArrayList<>();
        for (Category category : categories) {
            categoriesTitles.add(category.getName());
        }
        if (categoriesTitles.size() > 0)
            selectedCategory = categoriesTitles.get(0);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesTitles);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryChooser.setAdapter(dataAdapter);

        if (getIntent() != null && getIntent().hasExtra("type") && getIntent().hasExtra("id")) {
            //if we need to update transaction, we need to set fields
            Type type = Type.getType(getIntent().getStringExtra("type"));
            recevedTransaction = helper.getTransaction(type, getIntent().getIntExtra("id", 0));
            typeChooser.setSelection(type == Type.INCOME ? 0 : 1);
            int spinnerPosition = dataAdapter.getPosition(recevedTransaction.getCategory());
            categoryChooser.setSelection(spinnerPosition);
            amount.setText(String.valueOf(recevedTransaction.getAmount()));
            note.setText(recevedTransaction.getNote());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(recevedTransaction.getDate()));
            date.setText(generateDate(calendar));
            isDateChosen = true;
            needToUpdate = true;
        }

        add.setOnClickListener(v -> {
            String amountString = amount.getText().toString();
            String noteString = note.getText().toString();
            //Checking if fields are not empty, creating Transaction Model and writing it to the database
            if (isDateChosen && amountString.length() > 0) {
                Transaction transaction = new Transaction();
                transaction.setType(typeChooser.getSelectedItemPosition() == 0 ? Type.INCOME : Type.EXPENSE);
                transaction.setCategory(selectedCategory);
                transaction.setDate(generateMillis());
                transaction.setNote(noteString);
                transaction.setAmount(Long.parseLong(amountString));
                if (needToUpdate) {
                    if (hourOfDay == 0) {
                        transaction.setDate(recevedTransaction.getDate());
                    }
                    transaction.setId(recevedTransaction.getId());
                    helper.updateTransaction(transaction);
                    setResult(RESULT_OK);
                    finish();
                    Toast.makeText(this, R.string.transaction_added, Toast.LENGTH_SHORT).show();
                } else {
                    if (helper.insertTransaction(transaction)) {
                        setResult(RESULT_OK);
                        finish();
                        Toast.makeText(this, R.string.transaction_added, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, R.string.transaction_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, R.string.fields_error, Toast.LENGTH_SHORT).show();
            }
        });
        date.setOnClickListener(v -> {
            //open date chooser with selected date as current date
            Calendar calendar = Calendar.getInstance();
            @SuppressLint("WrongConstant") DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    @SuppressLint("WrongConstant")
    String generateDate(Calendar calendar) {
        return "" + addZeroIfNeeded(calendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                addZeroIfNeeded(calendar.get(Calendar.MINUTE)) + " " +
                addZeroIfNeeded(calendar.get(Calendar.MONTH) + 1) + "/" +
                addZeroIfNeeded(calendar.get(Calendar.DAY_OF_MONTH)) + "/" +
                calendar.get(Calendar.YEAR);
    }

    String generateDate() {
        return "" + addZeroIfNeeded(hourOfDay) + ":" + addZeroIfNeeded(minute) + " " + addZeroIfNeeded(month) + "/" + addZeroIfNeeded(dayOfMonth) + "/" + year;
    }

    Long generateMillis() {
        //generate time in millis for database
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, hourOfDay, minute);
        return calendar.getTimeInMillis();
    }

    String addZeroIfNeeded(int number) {
        return number < 10 ? "0" + number : String.valueOf(number);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        this.year = year;
        this.month = month + 1;
        isDateChosen = false;
        //open time picker dialog with current time set
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("WrongConstant") TimePickerDialog timePickerDialog = new TimePickerDialog(AddTransactionActivity.this, AddTransactionActivity.this,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        isDateChosen = true;
        date.setText(generateDate());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCategory = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
