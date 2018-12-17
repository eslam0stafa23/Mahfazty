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
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.Advanced.Academy.Mahfazty.utils.DrawerItems;

import java.util.Locale;


public class BaseDrawerActivity extends AppCompatActivity {
    private Drawer result = null;
    private Drawer.OnDrawerItemClickListener listener = (view, position, drawerItem) -> {
        //OnDrawerItemClickListener - open different activities on different menu item clicks
        if (drawerItem != null) {
            if (drawerItem.getIdentifier() == DrawerItems.OVERVIEW.getIdentifier()) {
                finish();
                Intent i = new Intent(BaseDrawerActivity.this, MainActivity.class);
                startActivity(i);
            } else if (drawerItem.getIdentifier() == DrawerItems.EXPENSES.getIdentifier()) {
                if (!(BaseDrawerActivity.this instanceof MainActivity)) {
                    finish();
                }
                Intent i = new Intent(BaseDrawerActivity.this, TransactionListActivity.class);
                i.putExtra("type", "expense");
                startActivity(i);
            } else if (drawerItem.getIdentifier() == DrawerItems.INCOME.getIdentifier()) {
                if (!(BaseDrawerActivity.this instanceof MainActivity)) {
                    finish();
                }
                Intent i = new Intent(BaseDrawerActivity.this, TransactionListActivity.class);
                i.putExtra("type", "income");
                startActivity(i);
            } else if (drawerItem.getIdentifier() == DrawerItems.CATEGORIES.getIdentifier()) {
                if (!(BaseDrawerActivity.this instanceof MainActivity)) {
                    finish();
                }
                Intent i = new Intent(BaseDrawerActivity.this, CategoriesActivity.class);
                startActivity(i);
            } else if (drawerItem.getIdentifier() == DrawerItems.EXIT.getIdentifier()) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            } else if (drawerItem.getIdentifier() == DrawerItems.ENGLISH.getIdentifier()) {
                changeLocale("en");
            } else if (drawerItem.getIdentifier() == DrawerItems.ARABIC.getIdentifier()) {
               changeLocale("ar");
            }

        }
        return false;
    };

    public void changeLocale(String locale) {
        Locale myLocale = new Locale(locale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        getSharedPreferences("money", 0).edit().putString("locale", locale).commit();
        Intent refresh = new Intent(this, SplashScreenActivity.class);
        startActivity(refresh);
        finish();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        //open side menu on hamburger button click
        findViewById(R.id.drawer).setOnClickListener(v -> {
            if (result != null) result.openDrawer();
        });
        createAnonymousDrawer(savedInstanceState);
    }

    public void createAnonymousDrawer(final Bundle savedInstanceState) {
        //creating side menu with items from enum class DrawerItems
        result = new DrawerBuilder()
                .withActivity(this)
                .withHasStableIds(true)
                .withActionBarDrawerToggle(false)
                .withHeader(R.layout.layout_header)
                .addDrawerItems(
                        DrawerItems.OVERVIEW.getDrawerItem(this),
                        DrawerItems.EXPENSES.getDrawerItem(this),
                        DrawerItems.INCOME.getDrawerItem(this),
                        DrawerItems.CATEGORIES.getDrawerItem(this),
                        new DividerDrawerItem(),
                        DrawerItems.ENGLISH.getDrawerItem(this),
                        DrawerItems.ARABIC.getDrawerItem(this),
                        new DividerDrawerItem(),
                        DrawerItems.EXIT.getDrawerItem(this)
                ).withOnDrawerItemClickListener(listener)
                .withSavedInstance(savedInstanceState)
                .build();
    }
}
