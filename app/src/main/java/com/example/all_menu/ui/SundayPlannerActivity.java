package com.example.all_menu.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.all_menu.R;
import com.example.all_menu.adapters.SavedHorAdapter;
import com.example.all_menu.models.MenuVerModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SundayPlannerActivity extends AppCompatActivity implements SavedHorAdapter.OnMoreOptionsClickListener{

    private String chosenMealType;

    private LinearLayout breakfastContainer;
    private LinearLayout lunchContainer;
    private LinearLayout dinnerContainer;
    private ActivityResultLauncher<Intent> selectedMealsLauncher;
    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunday_planner);


        TextView back_sunday_planner = findViewById(R.id.back_sunday_planner);
        RecyclerView recyclerView = findViewById(R.id.saved_hor_rec);
        TextView breakfastTextView = findViewById(R.id.tv_day_item0);
        TextView lunchTextView = findViewById(R.id.tv_day_item1);
        TextView dinnerTextView = findViewById(R.id.tv_day_item2);

        // Initialize LinearLayouts
        breakfastContainer = findViewById(R.id.breakfastContainer);
        lunchContainer = findViewById(R.id.lunchContainer);
        dinnerContainer = findViewById(R.id.dinnerContainer);

        // Click event for the Sunday Planner Back TextView
        back_sunday_planner.setOnClickListener(v -> {
            Intent intent = new Intent(SundayPlannerActivity.this, MealPlannerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("showButton", true);
            startActivity(intent);
        });

        // Retrieve the saved meals list from SharedPreferences
        sharedPreferences = getSharedPreferences("SavedMeals", Context.MODE_PRIVATE);

        List<MenuVerModel> savedMealsList = getSavedMealsFromSharedPreferences();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize and set adapter
        SavedHorAdapter adapter = new SavedHorAdapter(savedMealsList, this);
        recyclerView.setAdapter(adapter);


        breakfastTextView.setOnClickListener(v -> {
            chosenMealType = "Breakfast";
            showOptionsDialog();
        });

        lunchTextView.setOnClickListener(v -> {
            chosenMealType = "Lunch";
            showOptionsDialog();
        });

        dinnerTextView.setOnClickListener(v -> {
            chosenMealType = "Dinner";
            showOptionsDialog();
        });

        // Initialize the ActivityResultLauncher
        selectedMealsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            int mealImage = data.getIntExtra("mealImage", 0);
                            String mealTitle = data.getStringExtra("mealTitle");
                            if (mealImage != 0 && mealTitle != null) {
                                displayMealDetails(mealImage, mealTitle);
                            }
                        }
                    }
                }
        );

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

    public void onMoreOptionsClick(int mealImage, String mealTitle) {
        // Show the bottom sheet dialog with meal type options
        showBottomSheetDialog(mealImage, mealTitle);
    }

    private void showBottomSheetDialog(int mealImage, String mealTitle) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout_meal, null);
        bottomSheetView.setBackground(ContextCompat.getDrawable(this, R.drawable.dialog_bg));
        bottomSheetView.findViewById(R.id.option_breakfast).setOnClickListener(v -> {
            chosenMealType = "Breakfast";
            displayMealDetails(mealImage, mealTitle);
            bottomSheetDialog.dismiss();
        });
        bottomSheetView.findViewById(R.id.option_lunch).setOnClickListener(v -> {
            chosenMealType = "Lunch";
            displayMealDetails(mealImage, mealTitle);
            bottomSheetDialog.dismiss();
        });
        bottomSheetView.findViewById(R.id.option_dinner).setOnClickListener(v -> {
            chosenMealType = "Dinner";
            displayMealDetails(mealImage, mealTitle);
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void showOptionsDialog() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
        bottomSheetView.setBackground(ContextCompat.getDrawable(this, R.drawable.dialog_bg));
        bottomSheetView.findViewById(R.id.layout_from_Saved).setOnClickListener(v -> {
            // Handle "Add from saved meals" option
            // Show the list of saved meals
            showSavedMeals();
            bottomSheetDialog.dismiss();
        });
        bottomSheetView.findViewById(R.id.layout_from_All).setOnClickListener(v -> {
            // Handle "Add from all meals" option
            // Show the list of all meals
            showAllMeals();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void showAllMeals() {
        Intent intent = new Intent(this, AllMealsSelectionActivity.class);
        selectedMealsLauncher.launch(intent);
    }

    private void showSavedMeals() {

        Intent intent = new Intent(this, SavedMealsSelectionActivity.class);
        selectedMealsLauncher.launch(intent);
    }

    private void storeMealDetails(int mealImage, String mealTitle) {
        // Get SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("MealDetails", Context.MODE_PRIVATE);

        // Get the current quantity for the meal
        int currentQuantity = sharedPreferences.getInt(mealTitle + "_quantity", 0);

        // Increment the quantity by 1
        int quantity = currentQuantity + 1;

        // Store meal image, title, and quantity
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(mealTitle + "_image", mealImage);
        editor.putInt(mealTitle + "_quantity", quantity);
        editor.apply();
    }

    private void displayMealDetails(int mealImage, String mealTitle) {
        // Inflate your custom card layout
        View mealCardView = getLayoutInflater().inflate(R.layout.added_meal_card, null);

        // Set meal image and title
        ImageView ivAddedRecipe = mealCardView.findViewById(R.id.iv_added_recipe);
        TextView tvAddedMealTitle = mealCardView.findViewById(R.id.tv_added_meal_title);

        ivAddedRecipe.setImageResource(mealImage);
        tvAddedMealTitle.setText(mealTitle);

        storeMealDetails(mealImage, mealTitle);

        // Find the appropriate LinearLayout and add the card
        LinearLayout container;
        if (chosenMealType != null) {
            switch (chosenMealType) {
                case "Breakfast":
                    container = breakfastContainer;
                    break;
                case "Lunch":
                    container = lunchContainer;
                    break;
                case "Dinner":
                    container = dinnerContainer;
                    break;
                default:
                    Log.e("SundayPlannerActivity", "Invalid meal type");
                    return; // Invalid meal type
            }
            container.addView(mealCardView);
        } else {
            Log.e("SundayPlannerActivity", "chosenMealType is null");
        }
    }


}

