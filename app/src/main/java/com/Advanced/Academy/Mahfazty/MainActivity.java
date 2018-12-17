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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import com.Advanced.Academy.Mahfazty.models.Transaction;
import com.Advanced.Academy.Mahfazty.utils.Helper;
import com.Advanced.Academy.Mahfazty.utils.TransactionsDBHelper;

import java.util.ArrayList;

/*
this is the main activity for mahfazty app
it's responsible for the home screen and updating the values of Current cash, Expenses and Income
*/

public class MainActivity extends BaseDrawerActivity {
    TextView current;
    TextView income;
    TextView expenses;
    FloatingActionButton add;
    Long incomeValue, expensesValue, currentValue;
    TransactionsDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.setLocale(getBaseContext(), this);
        setContentView(R.layout.activity_main);
        helper = new TransactionsDBHelper(this);
        //Binding views
        current = (TextView) findViewById(R.id.current);
        income = (TextView) findViewById(R.id.income);
        expenses = (TextView) findViewById(R.id.expenses);
        add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(v -> startActivity(new Intent(this, AddTransactionActivity.class)));
        updateValues();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateValues();
    }

    void updateValues(){
        //get all transactions and count current cash, income and expenses
        ArrayList<Transaction> incomesList = helper.getAllIncomes();
        ArrayList<Transaction> expensesList = helper.getAllExpenses();
        incomeValue = 0L;
        expensesValue = 0L;
        currentValue = 0L;
        for (Transaction income : incomesList) {
            incomeValue += income.getAmount();
        }
        for (Transaction expense : expensesList) {
            expensesValue += expense.getAmount();
        }
        currentValue = incomeValue - expensesValue;
        current.setText(getString(R.string.pound, currentValue));
        income.setText(getString(R.string.pound, incomeValue));
        expenses.setText(getString(R.string.pound, expensesValue));
    }
}
