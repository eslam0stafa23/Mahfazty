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

import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.Advanced.Academy.Mahfazty.R;

/**
 * Enum class for simple side menu items management
 * https://github.com/mikepenz/MaterialDrawer - side menu library
 */

public enum DrawerItems {
    OVERVIEW(R.string.overview, 1, R.drawable.ic_perm_contact_calendar_black_24dp),
    EXPENSES(R.string.expenses, 2, R.drawable.ic_money_off_black_24dp),
    INCOME(R.string.income, 3, R.drawable.ic_attach_money_black_24dp),
    CATEGORIES(R.string.categories, 4, R.drawable.ic_list_black_24dp),
    ENGLISH(R.string.eng, 5, R.drawable.ic_language_black_24dp),
    ARABIC(R.string.ar, 6, R.drawable.ic_language_black_24dp),
    EXIT(R.string.exit, 7, R.drawable.ic_exit_to_app_black_24dp);

    private int nameId;
    private int identifier;
    private int drawableId;

    DrawerItems(int nameId, int identifier, int drawableId) {
        this.nameId = nameId;
        this.identifier = identifier;
        this.drawableId = drawableId;
    }

    public IDrawerItem getDrawerItem(Context context) {
        return new PrimaryDrawerItem()
                .withName(nameId)
                .withTextColor(context.getResources().getColor(R.color.text))
                .withIcon(context.getResources().getDrawable(drawableId))
                .withIdentifier(identifier)
                .withSelectable(false)
                .withSelectedColor(context.getResources().getColor(R.color.selected_text))
                .withSelectedTextColor(context.getResources().getColor(R.color.text));
    }

    public int getIdentifier() {
        return identifier;
    }
}
