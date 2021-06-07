package com.tolgahanoktay.panaromia;

public class CategoryModel {

    private int icon;
    private String category_name;

    public CategoryModel(int icon, String category_name) {
        this.icon = icon;
        this.category_name = category_name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
