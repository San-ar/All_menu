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

import java.util.List;

public class SundayPlannerActivity extends AppCompatActivity implements SavedHorAdapter.OnMoreOptionsClickListener{

    private String chosenMealType;

    private LinearLayout breakfastContainer;
    private LinearLayout lunchContainer;
    private LinearLayout dinnerContainer;
    private ActivityResultLauncher<Intent> selectedMealsLauncher;

    private static final String TAG = "SundayPlannerActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunday_planner);

        Log.d(TAG, "onCreate: Started");

        TextView back_sunday_planner = findViewById(R.id.back_sunday_planner);

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

        boolean isSundayPlannerSharedPreferencesEmpty = isSundayPlannerSharedPreferencesEmpty();
        Log.d(TAG, "onCreate: isSharedPreferencesEmpty: " + isSundayPlannerSharedPreferencesEmpty);

        // Retrieve saved meals list from intent extras
        List<MenuVerModel> savedMealsList = (List<MenuVerModel>) getIntent().getSerializableExtra("savedMeals");

        if (savedMealsList != null) {
            // Initialize RecyclerView and set adapter
            RecyclerView recyclerView = findViewById(R.id.saved_hor_rec);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            SavedHorAdapter adapter = new SavedHorAdapter(savedMealsList, this);
            recyclerView.setAdapter(adapter);
        } else {
            Log.e("SundayPlannerActivity", "Saved meals list is null");
        }

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

        boolean isSundayPlannerSharedPreferencesEmpty = isSundayPlannerSharedPreferencesEmpty();
        Log.d(TAG, "onCreate: isSharedPreferencesEmpty: " + isSundayPlannerSharedPreferencesEmpty);

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

    private boolean isSundayPlannerSharedPreferencesEmpty() {
        // Get SharedPreferences instance for the Sunday planner
        SharedPreferences sharedPreferences = getSharedPreferences("MealDetails", Context.MODE_PRIVATE);

        // Check if SharedPreferences is empty
        boolean isEmpty = sharedPreferences.getAll().isEmpty();
        Log.d(TAG, "isSundayPlannerSharedPreferencesEmpty: " + isEmpty);
        return isEmpty;
    }


}

