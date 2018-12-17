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
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import com.Advanced.Academy.Mahfazty.adapters.ExpandableListAdapter;
import com.Advanced.Academy.Mahfazty.models.Category;
import com.Advanced.Academy.Mahfazty.utils.CategoriesDBHelper;
import com.Advanced.Academy.Mahfazty.utils.Helper;

import java.util.ArrayList;



public class CategoriesActivity extends BaseDrawerActivity {
    ExpandableListView categoriesList;
    FloatingActionButton fab;

    private ExpandableListAdapter listAdapter;
    private CategoriesDBHelper helper;
    private ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.setLocale(getBaseContext(), this);
        setContentView(R.layout.activity_categories);
        //Binding views
        categoriesList = (ExpandableListView) findViewById(R.id.categoriesList);
        fab = (FloatingActionButton) findViewById(R.id.add);
        categories = new ArrayList<>();
        //initiating CategoriesDBHelper
        helper = new CategoriesDBHelper(this);
        //fill the list with the categories from the database
        categories.addAll(helper.getAllCategories());
        listAdapter = new ExpandableListAdapter(this, categories);
        categoriesList.setAdapter(listAdapter);
        categoriesList.setGroupIndicator(null);
        categoriesList.setChildIndicator(null);
        categoriesList.setChildDivider(getResources().getDrawable(R.color.transparent));
        categoriesList.setDivider(getResources().getDrawable(R.color.transparent));
        categoriesList.setDividerHeight(10);
        fab.setOnClickListener(v -> startActivityForResult(new Intent(CategoriesActivity.this, AddCategoryActivity.class), 1234));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            //if AddCategoryActivity says that category was added, we need to reload data in the list
            reloadList();
        }
    }

    public void reloadList() {
        categories.clear();
        categories.addAll(helper.getAllCategories());
        listAdapter = new ExpandableListAdapter(this, categories);
        categoriesList.setAdapter((android.widget.ExpandableListAdapter) null);
        categoriesList.destroyDrawingCache();
        categoriesList.setAdapter(listAdapter);
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
                    AddCategoryActivity.display(CategoriesActivity.this, id);
                    break;
                case 1:
                    //new dialog with ARE YOU SURE title
                    new AlertDialog.Builder(CategoriesActivity.this)
                            .setTitle(R.string.delete)
                            .setMessage(R.string.are_you_sure)
                            .setPositiveButton(android.R.string.yes, (dialog12, which12) -> {
                                helper.deleteCategory(id);
                                reloadList();
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
