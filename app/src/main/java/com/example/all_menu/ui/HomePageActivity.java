package com.example.all_menu.ui;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.all_menu.R;
import com.example.all_menu.adapters.MenuVerAdapter;
import com.example.all_menu.models.MenuVerModel;
import com.example.all_menu.models.MenuViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    RecyclerView homeVerticalRec;
    MenuVerAdapter homeVerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialize RecyclerView
        homeVerticalRec = findViewById(R.id.home_ver_rec);

        // Create ViewModel instance
        MenuViewModel menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        // Observe menu items LiveData
        // Update UI with menu items
        menuViewModel.getMenuItemsLiveData().observe(this, this::setupRecyclerView);
    }

    //Method for "See All" Textview
    public void openMenuActivity(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    // Initialize and set up the adapter
    private void setupRecyclerView(List<MenuVerModel> menuVerModelList) {
        // Initialize and set up the adapter
        homeVerAdapter = new MenuVerAdapter(this, menuVerModelList, null);
        homeVerticalRec.setAdapter(homeVerAdapter);
        homeVerticalRec.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        homeVerticalRec.setHasFixedSize(true);
        homeVerticalRec.setNestedScrollingEnabled(false);
    }


}