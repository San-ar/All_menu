package com.example.all_menu.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.all_menu.R;

public class YogurtDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_yogurt);

        TextView textView = findViewById(R.id.back_recipe_details);
        // Handle click event for the Recipe Details Back TextView
        textView.setOnClickListener(v -> finish());


    }
}
