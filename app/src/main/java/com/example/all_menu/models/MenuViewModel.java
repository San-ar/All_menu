package com.example.all_menu.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.all_menu.R;
import com.example.all_menu.models.MenuVerModel;

import java.util.ArrayList;
import java.util.List;

public class MenuViewModel extends ViewModel {
    private MutableLiveData<List<MenuVerModel>> menuItemsLiveData;

    public LiveData<List<MenuVerModel>> getMenuItemsLiveData() {
        if (menuItemsLiveData == null) {
            menuItemsLiveData = new MutableLiveData<>();
            // Load menu items initially
            loadMenuItems();
        }
        return menuItemsLiveData;
    }

    private void loadMenuItems() {

        List<MenuVerModel> menuVerModelList = new ArrayList<>();

        menuVerModelList.add(new MenuVerModel(R.drawable.b1_yogurt, "Breakfast", "190 - 200 kcal", "10", "Yogurt Parfait"));
        menuVerModelList.add(new MenuVerModel(R.drawable.b2_toast, "Breakfast", "220 - 250 kcal", "5", "Peanut Butter Banana Toast"));
        menuVerModelList.add(new MenuVerModel(R.drawable.b3_eggs, "Breakfast", "147 - 167 kcal", "12", "Scrambled Eggs with Spinach"));
        menuVerModelList.add(new MenuVerModel(R.drawable.b4_burrito, "Breakfast", "380 - 475 kcal", "35", "Breakfast Burrito"));
        menuVerModelList.add(new MenuVerModel(R.drawable.l1_veggie_bowl, "Lunch", "116 kcal", "10", "Cottage Cheese & Veggie Bowl"));
        menuVerModelList.add(new MenuVerModel(R.drawable.l2_lentil_salad, "Lunch", "231 - 241 kcal", "25", "Lentil Salad"));
        menuVerModelList.add(new MenuVerModel(R.drawable.l3_chicken_salad, "Lunch", "205 kcal", "20", "Easy Chicken Salad"));
        menuVerModelList.add(new MenuVerModel(R.drawable.l4_turkey_wrap, "Lunch", "243 - 303 kcal", "15", "Hummus & Turkey Wrap"));
        menuVerModelList.add(new MenuVerModel(R.drawable.d1_broccoli, "Dinner", "251 kcal", "25", "Broccoli & Chickpea Stir-Fry"));
        menuVerModelList.add(new MenuVerModel(R.drawable.d2_grill_chicken, "Dinner", "400 kcal", "30", "Grilled Chicken with Roasted Vegetables"));
        menuVerModelList.add(new MenuVerModel(R.drawable.d3_quinoa, "Dinner", "404 kcal", "30", "Quinoa & Black Bean Bowl"));
        menuVerModelList.add(new MenuVerModel(R.drawable.d4_salmon, "Dinner", "541 kcal", "40", "Salmon with Sweet Potato & Asparagus"));

        if (menuItemsLiveData == null) {
            menuItemsLiveData = new MutableLiveData<>();
        }

        // Update LiveData with the loaded menu items
        menuItemsLiveData.setValue(menuVerModelList);
    }
}
