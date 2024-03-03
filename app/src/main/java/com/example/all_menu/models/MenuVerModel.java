package com.example.all_menu.models;

import java.io.Serializable;

public class MenuVerModel implements Serializable {

    int iv_recipe;
    String tv_meal_label;
    String tv_calorie;
    String tv_time;
    String tv_meal_title;
    boolean isSelected;


    // Constructor to initialize menu item
    public MenuVerModel(int iv_recipe, String tv_meal_label, String tv_calorie, String tv_time, String tv_meal_title) {
        this.iv_recipe = iv_recipe;
        this.tv_meal_label = tv_meal_label;
        this.tv_calorie = tv_calorie;
        this.tv_time = tv_time;
        this.tv_meal_title = tv_meal_title;
        this.isSelected = false;
    }

    // Getter and setter methods for each variable
    public int getIv_recipe() {
        return iv_recipe;
    }

    public void setIv_recipe(int iv_recipe) {
        this.iv_recipe = iv_recipe;
    }

    public String getTv_meal_label() {
        return tv_meal_label;
    }

    public void setTv_meal_label(String tv_meal_label) {
        this.tv_meal_label = tv_meal_label;
    }

    public String getTv_calorie() {
        return tv_calorie;
    }

    public void setTv_calorie(String tv_calorie) {
        this.tv_calorie = tv_calorie;
    }

    public String getTv_time() {
        return tv_time;
    }

    public void setTv_time(String tv_time) {
        this.tv_time = tv_time;
    }

    public String getTv_meal_title() {
        return tv_meal_title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setTv_meal_title(String tv_meal_title) {
        this.tv_meal_title = tv_meal_title;
    }

    // Method to get the meal type (same as meal label)
    public String getMealType() { return tv_meal_label;
    }

}
