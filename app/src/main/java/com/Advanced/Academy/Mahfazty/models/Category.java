package com.Advanced.Academy.Mahfazty.models;

/*

* Designed and developed by
 * Eslam Mostafa Sayed
 * Amgad Mohamed Attia
 * Ashraf Mahmoud Abdulmaged
 * Amir Hussain Mostafa

as a graduation project for the year of 2017
Advanced Academy
*/

import java.io.Serializable;


public class Category implements Serializable{
    private Integer id;
    private String name;
    private String parentCategory;
    private Type type;

    public Category() {
        parentCategory = null;
    }

    public static Category of(String name, String parentCategory, Type type) {
        Category category = new Category();
        category.setName(name);
        category.setParentCategory(parentCategory);
        category.setType(type);
        return category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}
