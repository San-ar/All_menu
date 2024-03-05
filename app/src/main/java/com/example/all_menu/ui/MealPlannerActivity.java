package com.example.all_menu.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.all_menu.R;
import com.example.all_menu.adapters.SavedHorAdapter;
import com.example.all_menu.adapters.SelectMealsAdapter;
import com.example.all_menu.models.MenuVerModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MealPlannerActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_planner);

        TextView startDateTextView = findViewById(R.id.tv_start_date);
        TextView endDateTextView = findViewById(R.id.tv_end_date);

        TextView back_meal_planner = findViewById(R.id.back_meal_planner);

        RecyclerView recyclerView = findViewById(R.id.saved_hor_rec);

        Button btConfirmOrder = findViewById(R.id.bt_confirm_order);

        sharedPreferences = getSharedPreferences("SavedMeals", Context.MODE_PRIVATE);
        // Retrieve the saved meals list from SharedPreferences
        List<MenuVerModel> savedMealsList = getSavedMealsFromSharedPreferences();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Log the size of savedMealsList
        assert savedMealsList != null;
        Log.d("MealPlannerActivity", "Saved meals list size: " + savedMealsList.size());

        // Initialize and set adapter
        SavedHorAdapter adapter = new SavedHorAdapter(savedMealsList, null);
        recyclerView.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_planner);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                item.setChecked(true);
                finish();
                return true;

            } else if (item.getItemId() == R.id.bottom_search) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                item.setChecked(true);
                finish();
                return true;

            } else if (item.getItemId() == R.id.bottom_planner) {
                return true;

            } else if (item.getItemId() == R.id.bottom_notification) {
                return true;

            } else if (item.getItemId() == R.id.bottom_profile) {
                return true;

            }
            return false;
        });

        // Click event for the Meal Planner Back TextView
        back_meal_planner.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        // Calculate start and end dates of next week
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1); // Move to next day to avoid today's date
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysUntilNextSunday = Calendar.SUNDAY - currentDayOfWeek;
        if (daysUntilNextSunday <= 0) {
            daysUntilNextSunday += 7; // Already Sunday, so add 7 days
        }
        calendar.add(Calendar.DAY_OF_YEAR, daysUntilNextSunday);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 6); // Add 6 days to get to Saturday
        Date endDate = calendar.getTime();

        // Format the dates as "MMM dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);

        // Set the text views with the calculated dates
        startDateTextView.setText(formattedStartDate);
        endDateTextView.setText(formattedEndDate);




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

    public void previousWeekAction(View view) {
    }

    public void nextWeekAction(View view) {
    }

    public void sunday_planner_action(View view) {

        // Create the intent for SundayPlannerActivity
        Intent intent = new Intent(this, SundayPlannerActivity.class);

        // Pass the saved meals list as an intent extra
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onConfirmOrderButtonClick(View view) {
        Intent intent = new Intent(this, ReviewOrdersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
