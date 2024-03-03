package com.example.all_menu.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.all_menu.R;
import com.example.all_menu.adapters.SelectMealsAdapter;
import com.example.all_menu.models.MenuVerModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SavedMealsActivity extends AppCompatActivity implements SelectMealsAdapter.OnItemClickListener {

    private SharedPreferences sharedPreferences;
    private SelectMealsAdapter adapter;
    private Button bt_add_meal;
    private MenuVerModel selectedMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_meals);

        TextView back_select_saved = findViewById(R.id.back_saved_meals);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TextView emptyTextView = findViewById(R.id.emptyTextView);

        bt_add_meal = findViewById(R.id.bt_add_meal);


        back_select_saved.setOnClickListener(view -> finish());

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("SavedMeals", Context.MODE_PRIVATE);

        // Retrieve the saved meals list from SharedPreferences
        List<MenuVerModel> savedMealsList = getSavedMealsFromSharedPreferences();

        // Initialize the adapter with the saved meals list
        adapter = new SelectMealsAdapter(this, savedMealsList);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Set listener to handle item clicks
        adapter.setOnItemClickListener(this);

        // Check if the saved meals list is empty
        if (savedMealsList.isEmpty()) {
            // Show empty text if the list is empty
            recyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            // Hide empty text and display the RecyclerView if the list is not empty
            recyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }
    }

    private List<MenuVerModel> getSavedMealsFromSharedPreferences() {
        // Retrieve the saved meals set from SharedPreferences
        Set<String> savedMealsSet = sharedPreferences.getStringSet("savedMeals", new HashSet<>());

        // Convert the set to a list of MenuVerModel objects
        List<MenuVerModel> savedMealsList = new ArrayList<>();
        for (String meal : savedMealsSet) {
            // Parse the meal string and create a MenuVerModel object
            String[] parts = meal.split(",");
            if (parts.length == 5) {
                int imageResId = Integer.parseInt(parts[0]);
                String mealType = parts[1];
                String calorie = parts[2];
                String time = parts[3];
                String mealTitle = parts[4];
                savedMealsList.add(new MenuVerModel(imageResId, mealType, calorie, time, mealTitle));
            }
        }
        return savedMealsList;
    }

    public void onItemClick(int position) {
        // Toggle the selection state of the clicked item
        adapter.toggleSelection(position);

        // Store the selected meal
        selectedMeal = adapter.getItem(position);

        // Show the "Add Meal" button when an item is clicked
        bt_add_meal.setVisibility(View.VISIBLE);
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
}
