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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.Advanced.Academy.Mahfazty.adapters.TransactionsListAdapter;
import com.Advanced.Academy.Mahfazty.models.Transaction;
import com.Advanced.Academy.Mahfazty.models.Type;
import com.Advanced.Academy.Mahfazty.utils.Helper;
import com.Advanced.Academy.Mahfazty.utils.TransactionsDBHelper;

import java.util.ArrayList;


public class TransactionListActivity extends BaseDrawerActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    TextView title;

    private TransactionsListAdapter adapter;
    private ArrayList<Transaction> transactions;
    private TransactionsDBHelper helper;
    private Type type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.setLocale(getBaseContext(), this);
        setContentView(R.layout.activity_transactions);
        //binding views
        fab = (FloatingActionButton) findViewById(R.id.add);
        recyclerView = (RecyclerView) findViewById(R.id.transactions_list);
        title = (TextView) findViewById(R.id.title);
        //getting type - expense or income.
        type = Type.getType(getIntent().getStringExtra("type"));
        //set title of activity depends on received type
        title.setText(type == Type.EXPENSE ? R.string.expenses : R.string.income);
        helper = new TransactionsDBHelper(this);
        //set transactions list with expenses or income depends on received type
        transactions = type == Type.EXPENSE ? helper.getAllExpenses() : helper.getAllIncomes();
        adapter = new TransactionsListAdapter(this, transactions);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        //set list
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(v -> startActivityForResult(new Intent(this, AddTransactionActivity.class), 111));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            //if AddTransactionActivity says that transaction is added successfully then reload data from db
            transactions.clear();
            transactions.addAll(type == Type.EXPENSE ? helper.getAllExpenses() : helper.getAllIncomes());
            adapter.notifyDataSetChanged();
        }
    }

    public void toggleSelection(Integer id) {
        //dialog builder that triggers from adapter. Id is the id of category
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle(R.string.choose);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        arrayAdapter.add(getString(R.string.change));
        arrayAdapter.add(getString(R.string.remove));
        builderSingle.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            switch (which) {
                case 0:
                    AddTransactionActivity.display(TransactionListActivity.this, type, id);
                    break;
                case 1:
                    //new dialog with ARE YOU SURE title
                    new AlertDialog.Builder(TransactionListActivity.this)
                            .setTitle(R.string.delete)
                            .setMessage(R.string.are_you_sure)
                            .setPositiveButton(android.R.string.yes, (dialog12, which12) -> {
                                helper.deleteTransaction(type, id);
                                transactions.clear();
                                transactions.addAll(type == Type.EXPENSE ? helper.getAllExpenses() : helper.getAllIncomes());
                                adapter.notifyDataSetChanged();
                                // continue with delete
                            })
                            .setNegativeButton(android.R.string.no, (dialog1, which1) -> {
                                // do nothing
                                dialog1.dismiss();
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    break;
            }

        });
        builderSingle.show();

    }
}
