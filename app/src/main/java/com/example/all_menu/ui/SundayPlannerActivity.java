package com.example.all_menu.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.all_menu.R;
import com.example.all_menu.adapters.SavedHorAdapter;
import com.example.all_menu.models.MenuVerModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class SundayPlannerActivity extends AppCompatActivity {

    private String chosenMealType;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunday_planner);

        TextView back_sunday_planner = findViewById(R.id.back_sunday_planner);

        TextView breakfastTextView = findViewById(R.id.tv_day_item0);
        TextView lunchTextView = findViewById(R.id.tv_day_item1);
        TextView dinnerTextView = findViewById(R.id.tv_day_item2);

        // Click event for the Sunday Planner Back TextView
        back_sunday_planner.setOnClickListener(v -> finish());

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
    }
}
