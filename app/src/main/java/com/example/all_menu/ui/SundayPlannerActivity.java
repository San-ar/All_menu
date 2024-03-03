package com.example.all_menu.ui;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
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
import java.util.Objects;

public class SundayPlannerActivity extends AppCompatActivity {

    private String chosenMealType;

    private LinearLayout breakfastContainer;
    private LinearLayout lunchContainer;
    private LinearLayout dinnerContainer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunday_planner);

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
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // Retrieve saved meals list from intent extras
        List<MenuVerModel> savedMealsList = (List<MenuVerModel>) getIntent().getSerializableExtra("savedMeals");

        if (savedMealsList != null) {
            // Initialize RecyclerView and set adapter
            RecyclerView recyclerView = findViewById(R.id.saved_hor_rec);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            SavedHorAdapter adapter = new SavedHorAdapter(savedMealsList);
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
    }

    private void showSavedMeals() {

        Intent intent = new Intent(SundayPlannerActivity.this, SavedMealsActivity.class);
        savedMealsLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> savedMealsLauncher = registerForActivityResult(
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

    private void displayMealDetails(int mealImage, String mealTitle) {
        // Inflate your custom card layout
        View mealCardView = getLayoutInflater().inflate(R.layout.added_meal_card, null);

        // Set meal image and title
        ImageView ivAddedRecipe = mealCardView.findViewById(R.id.iv_added_recipe);
        TextView tvAddedMealTitle = mealCardView.findViewById(R.id.tv_added_meal_title);

        ivAddedRecipe.setImageResource(mealImage);
        tvAddedMealTitle.setText(mealTitle);

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

