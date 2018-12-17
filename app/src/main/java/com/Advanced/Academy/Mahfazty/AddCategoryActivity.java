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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.Advanced.Academy.Mahfazty.models.Category;
import com.Advanced.Academy.Mahfazty.models.Type;
import com.Advanced.Academy.Mahfazty.utils.CategoriesDBHelper;
import com.Advanced.Academy.Mahfazty.utils.Helper;

import java.util.ArrayList;


public class AddCategoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView add;
    ImageView close;
    EditText name;
    Spinner parent;
    RadioButton income;
    RadioButton expense;

    public static void display(Activity context, Integer category) {
        Intent i = new Intent(context, AddCategoryActivity.class);
        i.putExtra("id", category);
        context.startActivityForResult(i, 1234);
    }

    Boolean needToUpdate = false;
    Category category;
    String parentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.setLocale(getBaseContext(), this);
        setContentView(R.layout.activity_add_category);
        CategoriesDBHelper dbHelper = new CategoriesDBHelper(AddCategoryActivity.this);
        //binding views
        add = (ImageView) findViewById(R.id.add);
        close = (ImageView) findViewById(R.id.close);
        name = (EditText) findViewById(R.id.name);
        parent = (Spinner) findViewById(R.id.parent);
        income = (RadioButton) findViewById(R.id.income);
        expense = (RadioButton) findViewById(R.id.expense);
        close.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        parent.setOnItemSelectedListener(this);
        //setting up adapter with categories from the database
        CategoriesDBHelper catHelper = new CategoriesDBHelper(this);
        ArrayList<Category> categories = catHelper.getAllCategories();
        ArrayList<String> categoriesTitles = new ArrayList<>();
        for (Category category : categories) {
            categoriesTitles.add(category.getName());
        }
        if (categoriesTitles.size() > 0)
            parentName = categoriesTitles.get(0);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesTitles);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parent.setAdapter(dataAdapter);


        if (getIntent() != null && getIntent().hasExtra("id")) {
            category = dbHelper.getCategory(getIntent().getIntExtra("id", 0));
            name.setText(category.getName());

            if (category.getParentCategory() != null) {
                int spinnerPosition = dataAdapter.getPosition(category.getParentCategory());
                parent.setSelection(spinnerPosition);
                parentName = category.getParentCategory();
            }
            income.setChecked(category.getType() == Type.INCOME);
            expense.setChecked(category.getType() == Type.EXPENSE);
            needToUpdate = true;
        }
        add.setOnClickListener(v -> {
            String categoryName = name.getText().toString();
            Type type = income.isChecked() ? Type.INCOME : Type.EXPENSE;
            String parentName = null;
            //check if category name field is not empty and write value to the database and then finish activity
            if (!categoryName.isEmpty()) {
                if (needToUpdate) {
                    Category category = new Category();
                    category.setId(this.category.getId());
                    category.setName(categoryName);
                    category.setParentCategory(parentName);
                    category.setType(type);
                    dbHelper.updateCategory(category);
                    Toast.makeText(this, R.string.category_added, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    if (dbHelper.insertCategory(Category.of(categoryName, parentName, type))) {
                        Toast.makeText(this, R.string.category_added, Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, R.string.category_adding_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, R.string.fields_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parentName = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
