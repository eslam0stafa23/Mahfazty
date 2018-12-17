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


public enum Type {
    EXPENSE("expense"), INCOME("income");
    private String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

   public static Type getType(String value) {
        switch (value) {
            case "expense":
                return EXPENSE;
            case "income":
                return INCOME;
            default:
                return null;
        }
    }
}
