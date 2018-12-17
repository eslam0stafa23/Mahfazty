package com.Advanced.Academy.Mahfazty.adapters;

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
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Advanced.Academy.Mahfazty.CategoriesActivity;
import com.Advanced.Academy.Mahfazty.R;
import com.Advanced.Academy.Mahfazty.models.Category;

import java.util.ArrayList;
import java.util.HashMap;


public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Activity _context;
    private ArrayList<String> headersList; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<String>> childsList;
    private HashMap<Integer, String> childsIds;


    public ExpandableListAdapter(Activity context, ArrayList<Category> categories) {
        this._context = context;
        headersList = new ArrayList<>();
        childsList = new HashMap<>();
        childsIds = new HashMap<>();
        for (Category category : categories) {
            if (category.getParentCategory() == null) {
                if (!headersList.contains(category.getName())) {
                    headersList.add(category.getName());
                    childsList.put(category.getName(), new ArrayList<String>());
                }
            } else {
                if (!headersList.contains(category.getParentCategory())) {
                    headersList.add(category.getParentCategory());
                    childsList.put(category.getParentCategory(), new ArrayList<String>());
                }
                childsIds.put(category.getId(), category.getName());
                ArrayList<String> categoryChilds = childsList.get(category.getParentCategory());
                categoryChilds.add(category.getName());
                childsList.put(category.getParentCategory(), categoryChilds);
            }
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.childsList.get(this.headersList.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_categories_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.name);

        txtListChild.setText(childText);
        convertView.setOnLongClickListener(v -> {
            Integer category = 0;
            for (Integer id : childsIds.keySet()) {
                if (childsIds.get(id).equals(childText)) {
                    category = id;
                    break;
                }
            }
            ((CategoriesActivity) _context).toggleSelection(category);
            return false;
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childsList.get(this.headersList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.headersList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.headersList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_categories_header, null);
        }
        //set Group title and icon. Icon can be arrow_up or arrow_down - if group is expanded or collapsed
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.name);
        lblListHeader.setText(headerTitle);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.action);
        imageView.setImageResource(isExpanded ? R.drawable.ic_keyboard_arrow_up_white_36dp : R.drawable.ic_keyboard_arrow_down_white_36dp);

        return convertView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
