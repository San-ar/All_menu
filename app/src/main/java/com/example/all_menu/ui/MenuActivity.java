package com.example.all_menu.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.all_menu.R;
import com.example.all_menu.adapters.MenuVerAdapter;
import com.example.all_menu.models.MenuVerModel;
import com.example.all_menu.models.MenuViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MenuActivity extends AppCompatActivity {

    RecyclerView menuVerticalRec;
    MenuVerAdapter menuVerAdapter;
    SearchView searchView;
    TextView textView;

    private Button btnAll, btnBreakfast, btnLunch, btnDinner;
    private View selectedOptionIndicator;

    private static final String ALL_MEALS = "All";
    private static final String BREAKFAST = "Breakfast";
    private static final String LUNCH = "Lunch";
    private static final String DINNER = "Dinner";

    private final List<MenuVerModel> savedMealsList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_menu);

        // Initialize views
        menuVerticalRec = findViewById(R.id.menu_ver_rec);

        searchView = findViewById(R.id.search);

        textView = findViewById(R.id.back_all_menu);
        btnAll = findViewById(R.id.btnAll);
        btnBreakfast = findViewById(R.id.btnBreakfast);
        btnLunch = findViewById(R.id.btnLunch);
        btnDinner = findViewById(R.id.btnDinner);
        selectedOptionIndicator = findViewById(R.id.selectedOptionIndicator);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                item.setChecked(true);
                startActivity(intent);
                finish();
                return true;

            } else if (item.getItemId() == R.id.bottom_search) {
                return true;

            } else if (item.getItemId() == R.id.bottom_planner) {
                Intent intent = new Intent(getApplicationContext(), MealPlannerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                item.setChecked(true);
                finish();
                return true;

            } else if (item.getItemId() == R.id.bottom_notification) {
                return true;

            } else if (item.getItemId() == R.id.bottom_profile) {
                return true;

            }
            return false;
        });


        // Create ViewModel instance
        MenuViewModel menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        // Observe menu items LiveData
        // Update UI with menu items
        menuViewModel.getMenuItemsLiveData().observe(this, this::setupRecyclerView);

        setupSearchView();

        emptyPlanner();

        // Click listeners for filter buttons
        btnAll.setOnClickListener(v -> filterMenuItems(ALL_MEALS));
        btnBreakfast.setOnClickListener(v -> filterMenuItems(BREAKFAST));
        btnLunch.setOnClickListener(v -> filterMenuItems(LUNCH));
        btnDinner.setOnClickListener(v -> filterMenuItems(DINNER));

        // Handle click event for the All Menu Back TextView
        textView.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        // By default, select "All" option in filter menu
        ViewTreeObserver viewTreeObserver = btnAll.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // The listener removed to avoid multiple calls
                btnAll.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // Call selectOption with the initialized buttons
                selectOption(btnAll);
            }
        });


    }


    // Method to save a meal to SharedPreferences
    public void saveMealToSharedPreferences(MenuVerModel meal) {
        // Retrieve the existing saved meals set
        SharedPreferences sharedPreferences = getSharedPreferences("SavedMeals", Context.MODE_PRIVATE);
        Set<String> savedMealsSet = sharedPreferences.getStringSet("savedMeals", new HashSet<>());

        // Convert the meal to a string representation
        String mealString = meal.getIv_recipe() + "," + meal.getMealType() + "," + meal.getTv_calorie() + "," + meal.getTv_time() + "," + meal.getTv_meal_title();

        // Add the new meal to the set
        savedMealsSet.add(mealString);

        // Save the updated set back to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("savedMeals", savedMealsSet);
        editor.apply();
    }

    public void removeMealFromSharedPreferences(MenuVerModel meal) {
        // Retrieve the existing saved meals set
        SharedPreferences sharedPreferences = getSharedPreferences("SavedMeals", Context.MODE_PRIVATE);
        Set<String> savedMealsSet = sharedPreferences.getStringSet("savedMeals", new HashSet<>());

        // Convert the meal to a string representation
        String mealString = meal.getIv_recipe() + "," + meal.getMealType() + "," + meal.getTv_calorie() + "," + meal.getTv_time() + "," + meal.getTv_meal_title();

        // Remove the meal from the set
        savedMealsSet.remove(mealString);

        // Save the updated set back to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("savedMeals", savedMealsSet);
        editor.apply();
    }


    // Initialize and set up the adapter
    private void setupRecyclerView(List<MenuVerModel> menuVerModelList) {
        // Initialize and set up the adapter
        menuVerAdapter = new MenuVerAdapter(this, menuVerModelList, this);
        menuVerticalRec.setAdapter(menuVerAdapter);
        menuVerticalRec.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        menuVerticalRec.setHasFixedSize(true);
        menuVerticalRec.setNestedScrollingEnabled(false);

        menuVerAdapter.setSavedMealsList(savedMealsList);
    }

    // Method to filter menu items based on meal type
    private void filterMenuItems(String mealType) {
        // Create ViewModel instance
        MenuViewModel menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        // Observe menu items LiveData
        menuViewModel.getMenuItemsLiveData().observe(this, menuVerModelList -> {
            List<MenuVerModel> filteredList = new ArrayList<>();

            if (mealType.equals(ALL_MEALS)) {
                // If "All" is selected, show all menu items
                filteredList.addAll(menuVerModelList);
            } else {
                // Filter menu items based on the selected meal type
                for (MenuVerModel item : menuVerModelList) {
                    if (item.getMealType().equalsIgnoreCase(mealType)) {
                        filteredList.add(item);
                    }
                }
            }

            // Update adapter with filtered list
            if (menuVerAdapter == null) {
                // Initialize adapter if null
                menuVerAdapter = new MenuVerAdapter(this, filteredList, null);
                menuVerticalRec.setAdapter(menuVerAdapter);
            } else {
                // Update existing adapter with filtered list
                menuVerAdapter.setMenuList(filteredList);
                menuVerAdapter.notifyDataSetChanged();
            }

            // Update UI to indicate the selected option
            switch (mealType) {
                case ALL_MEALS:
                    selectOption(btnAll);
                    break;
                case BREAKFAST:
                    selectOption(btnBreakfast);
                    break;
                case LUNCH:
                    selectOption(btnLunch);
                    break;
                case DINNER:
                    selectOption(btnDinner);
                    break;
            }
        });
    }

    private void selectOption(Button button) {
        // Update button text color
        int yellowColor = ContextCompat.getColor(this, R.color.yellow);
        int blackColor = ContextCompat.getColor(this, android.R.color.black);

        btnAll.setTextColor(button == btnAll ? yellowColor : blackColor);
        btnBreakfast.setTextColor(button == btnBreakfast ? yellowColor : blackColor);
        btnLunch.setTextColor(button == btnLunch ? yellowColor : blackColor);
        btnDinner.setTextColor(button == btnDinner ? yellowColor : blackColor);

        // Update selected option indicator position
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) selectedOptionIndicator.getLayoutParams();
        params.width = button.getWidth();
        params.leftMargin = button.getLeft();
        selectedOptionIndicator.setLayoutParams(params);
    }

    // Method to set up the search view
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Not used, as we want to filter instantly while typing
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter menu items based on the search query
                filterMenuItemsBySearch(newText);
                return true;
            }
        });
    }

    // Method to filter menu items based on search query
    private void filterMenuItemsBySearch(String query) {
        // Create ViewModel instance
        MenuViewModel menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        // Get the current list of menu items from the ViewModel
        List<MenuVerModel> menuVerModelList = menuViewModel.getMenuItemsLiveData().getValue();

        if (menuVerModelList != null) {
            List<MenuVerModel> filteredList = new ArrayList<>();
            for (MenuVerModel item : menuVerModelList) {
                if (item.getTv_meal_title().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            // Update adapter with filtered list
            if (menuVerAdapter == null) {
                // Initialize adapter if null
                menuVerAdapter = new MenuVerAdapter(this, filteredList, null);
                menuVerticalRec.setAdapter(menuVerAdapter);
            } else {
                // Update existing adapter with filtered list
                menuVerAdapter.setMenuList(filteredList);
                menuVerAdapter.notifyDataSetChanged();
            }
        }
    }

    private void emptyPlanner() {
        SharedPreferences sharedPreferences = getSharedPreferences("MealDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all the data
        editor.apply(); // Apply changes
    }
}


