package com.example.all_menu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.all_menu.R;
import com.example.all_menu.adapters.SelectMealsAdapter;
import com.example.all_menu.models.MenuVerModel;
import com.example.all_menu.models.MenuViewModel;

import java.util.ArrayList;
import java.util.List;

public class AllMealsSelectionActivity extends AppCompatActivity implements SelectMealsAdapter.OnItemClickListener{

    RecyclerView recyclerView_all;
    SelectMealsAdapter selectMealsAdapter;
    SearchView search_all_selection;
    Button bt_add_meal_all;

    private Button btnAll, btnBreakfast, btnLunch, btnDinner;
    View selectedOptionIndicator;

    private static final String ALL_MEALS = "All";
    private static final String BREAKFAST = "Breakfast";
    private static final String LUNCH = "Lunch";
    private static final String DINNER = "Dinner";

    MenuVerModel selectedMeal;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_meals_selection);

        TextView back_select_all = findViewById(R.id.back_select_meals_all);

        search_all_selection = findViewById(R.id.search_all_selection);

        recyclerView_all = findViewById(R.id.recyclerView_all);
        bt_add_meal_all = findViewById(R.id.bt_add_meal_all);

        btnAll = findViewById(R.id.btnAll);
        btnBreakfast = findViewById(R.id.btnBreakfast);
        btnLunch = findViewById(R.id.btnLunch);
        btnDinner = findViewById(R.id.btnDinner);
        selectedOptionIndicator = findViewById(R.id.selectedOptionIndicator);

        back_select_all.setOnClickListener(view -> finish());

        MenuViewModel menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        menuViewModel.getMenuItemsLiveData().observe(this, this::setupRecyclerView);

        setupSearchView();

        // Click listeners for filter buttons
        btnAll.setOnClickListener(v -> filterMenuItems(ALL_MEALS));
        btnBreakfast.setOnClickListener(v -> filterMenuItems(BREAKFAST));
        btnLunch.setOnClickListener(v -> filterMenuItems(LUNCH));
        btnDinner.setOnClickListener(v -> filterMenuItems(DINNER));

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

    private void setupRecyclerView(List<MenuVerModel> menuVerModels) {
        selectMealsAdapter = new SelectMealsAdapter(this, menuVerModels);
        recyclerView_all.setAdapter(selectMealsAdapter);
        recyclerView_all.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView_all.setHasFixedSize(true);
        recyclerView_all.setNestedScrollingEnabled(false);
        selectMealsAdapter.setOnItemClickListener(this);
    }

    public void onItemClick(int position) {
        // Toggle the selection state of the clicked item
        selectMealsAdapter.toggleSelection(position);

        // Store the selected meal
        selectedMeal = selectMealsAdapter.getItem(position);

        // Show the "Add Meal" button when an item is clicked
        bt_add_meal_all.setVisibility(View.VISIBLE);
    }

    // Button click handler method
    public void onAddMealButtonClick(View view) {
        if (selectedMeal != null) {
            // Pass the selected meal's image and title to the SundayPlannerActivity
            Intent intent = new Intent();
            intent.putExtra("mealImage", selectedMeal.getIv_recipe());
            intent.putExtra("mealTitle", selectedMeal.getTv_meal_title());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    // Method to set up the search view
    private void setupSearchView() {
        search_all_selection.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
            if (selectMealsAdapter == null) {
                // Initialize adapter if null
                selectMealsAdapter = new SelectMealsAdapter(this, filteredList);
                recyclerView_all.setAdapter(selectMealsAdapter);
            } else {
                // Update existing adapter with filtered list
                selectMealsAdapter.setMenuList(filteredList);
                selectMealsAdapter.notifyDataSetChanged();
            }
        }
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
            if (selectMealsAdapter == null) {
                // Initialize adapter if null
                selectMealsAdapter = new SelectMealsAdapter(this, filteredList);
                recyclerView_all.setAdapter(selectMealsAdapter);
            } else {
                // Update existing adapter with filtered list
                selectMealsAdapter.setMenuList(filteredList);
                selectMealsAdapter.notifyDataSetChanged();
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

}
