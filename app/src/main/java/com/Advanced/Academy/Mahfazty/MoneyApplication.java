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

import android.app.Application;

import com.Advanced.Academy.Mahfazty.models.Category;
import com.Advanced.Academy.Mahfazty.models.Type;
import com.Advanced.Academy.Mahfazty.utils.CategoriesDBHelper;

import java.util.ArrayList;
import java.util.Arrays;


public class MoneyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (getSharedPreferences("money", 0).getBoolean("isFirstLaunch", true)) {
            //it is first launch, let's fill db with default categories
            writeEnglishCategories();
            getSharedPreferences("money", 0).edit().putBoolean("isFirstLaunch", false).apply();
        }
    }

    void writeEnglishCategories() {
        ArrayList<String[]> childValues = new ArrayList<>();
        String[] parents = getResources().getStringArray(R.array.parent_values);
        CategoriesDBHelper helper = new CategoriesDBHelper(this);
        String[] income = getResources().getStringArray(R.array.income_values);
        String[] food = getResources().getStringArray(R.array.expense_food_values);
        String[] bills = getResources().getStringArray(R.array.expense_bills_values);
        String[] transportation = getResources().getStringArray(R.array.expense_transportation_values);
        String[] shopping = getResources().getStringArray(R.array.expense_shopping_values);
        String[] entertainment = getResources().getStringArray(R.array.expense_entertainment_values);
        String[] health = getResources().getStringArray(R.array.expense_health_values);
        String[] gifts = getResources().getStringArray(R.array.expense_gifts_values);
        String[] education = getResources().getStringArray(R.array.expense_education_values);
        String[] other = getResources().getStringArray(R.array.expense_other_values);
        childValues.addAll(Arrays.asList(income, food, bills, transportation, shopping,
                entertainment, health, gifts, education, other));
        int id = 0;
        for (String parent : parents) {
            Category category = Category.of(parent, null, id == 0 ? Type.INCOME : Type.EXPENSE);
            helper.insertCategory(category);
            for (String child : childValues.get(id)) {
                Category childCategory = Category.of(child, parent, id == 0 ? Type.INCOME : Type.EXPENSE);
                helper.insertCategory(childCategory);
            }
            id++;
        }
    }
}
