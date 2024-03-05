package com.example.all_menu.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.all_menu.R;
import com.example.all_menu.adapters.ReviewOrderAdapter;
import com.example.all_menu.models.MenuVerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReviewOrdersActivity extends AppCompatActivity {

    private List<MenuVerModel> mealList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_order);

        TextView back_review_order = findViewById(R.id.back_review_order);

        back_review_order.setOnClickListener(v -> {
            Intent intent = new Intent(this, MealPlannerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        // Initialize RecyclerView and adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView_review_meals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mealList = new ArrayList<>();
        ReviewOrderAdapter adapter = new ReviewOrderAdapter(mealList, this, this::onQuantityChanged);
        recyclerView.setAdapter(adapter);

        // Retrieve meal details from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MealDetails", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            // Extract meal details from SharedPreferences
            if (entry.getKey().endsWith("_image")) {
                int mealImage = sharedPreferences.getInt(entry.getKey(), 0);
                String mealTitle = entry.getKey().replace("_image", "");
                int quantity = sharedPreferences.getInt(mealTitle + "_quantity", 0);
                // Add meal to the list based on quantity
                if (quantity > 0) {
                    MenuVerModel meal = new MenuVerModel(mealImage, "", "", "", mealTitle);
                    meal.setQuantity(quantity);
                    mealList.add(meal);
                }
            }
        }

        adapter.notifyDataSetChanged();

        // Update total price
        updateTotalPrice();
    }

    public void onQuantityChanged() {
        // Update the total price whenever the quantity changes
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double totalPrice = calculateTotalPrice();
        // Update the TextView with the total price
        TextView totalPriceTextView = findViewById(R.id.review_total_price);
        totalPriceTextView.setText(String.format("%.2f", totalPrice));
    }

    private double calculateTotalPrice() {
        double totalPrice = 0;
        for (MenuVerModel meal : mealList) {
            totalPrice += 20.00 * meal.getQuantity();
        }
        return totalPrice;
    }
}


