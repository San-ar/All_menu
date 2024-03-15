package com.example.all_menu.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.all_menu.R;
import com.example.all_menu.models.MenuVerModel;
import com.example.all_menu.ui.MenuActivity;
import com.example.all_menu.ui.YogurtDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class MenuVerAdapter extends RecyclerView.Adapter<MenuVerAdapter.ViewHolder> {

    Context context;
    List<MenuVerModel> list;
    List<MenuVerModel> savedMealsList;
    private final MenuActivity menuActivity;
    private static final String ICON_STATE_KEY = "icon_state";


    // Constructor to initialize the adapter with context and menu list
    public MenuVerAdapter(Context context, List<MenuVerModel> list, MenuActivity menuActivity) {
        this.context = context;
        this.list = list;
        this.savedMealsList = new ArrayList<>();
        this.menuActivity = menuActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create ViewHolder
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_card, parent, false));
    }

    // Bind data to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MenuVerModel item = list.get(position);
        holder.iv_recipe.setImageResource(item.getIv_recipe());
        holder.meal_label.setText(item.getTv_meal_label());
        holder.calorie.setText(item.getTv_calorie());
        holder.time.setText(item.getTv_time());
        holder.meal_title.setText(item.getTv_meal_title());

        // If the meal is saved
        boolean isSaved = isMealSaved(item);

        // Update save icon state
        updateIconState(holder.iv_save_icon, item);

        // Click listener for the save icon
        holder.iv_save_icon.setOnClickListener(v -> {

            // Toggle the save state
            boolean isIconSaved = isMealSaved(item);
            isIconSaved = !isIconSaved;
            // Save icon state to SharedPreferences
            SharedPreferences.Editor editor = context.getSharedPreferences("IconState", Context.MODE_PRIVATE).edit();
            editor.putBoolean(ICON_STATE_KEY + item.getTv_meal_title(), isIconSaved);
            editor.apply();
            // Update icon state
            updateIconState(holder.iv_save_icon, item);

            // Handle saving/removing meal
            if (isSaved) {
                savedMealsList.remove(item);// Remove the meal from the saved list
                menuActivity.removeMealFromSharedPreferences(item);// Remove the meal from the SharedPreferences
                Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();
            } else {
                savedMealsList.add(item); // Add the meal to the saved list
                menuActivity.saveMealToSharedPreferences(item); // Save the meal to SharedPreferences
                Toast.makeText(context, "Item saved", Toast.LENGTH_SHORT).show();
            }
            // Notify the adapter that the data set has changed
            notifyDataSetChanged();
        });


        // Check if the meal title is "Yogurt Parfait"
        if (item.getTv_meal_title().equals("Yogurt Parfait")) {
            holder.itemView.setOnClickListener(v -> {
                // Start the Yogurt Parfait Meal Details activity when clicked
                Intent intent = new Intent(context, YogurtDetailsActivity.class);
                context.startActivity(intent);
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Method to update the menu list with filtered data
    public void setMenuList(List<MenuVerModel> filteredList) {
        this.list = filteredList;
        notifyDataSetChanged();
    }

    //Update the list of saved meals in the adapter.
    public void setSavedMealsList(List<MenuVerModel> savedMealsList) {
        this.savedMealsList = savedMealsList;
    }

    // Method to check if a meal is saved
    private boolean isMealSaved(MenuVerModel item) {
        // If the item exists in the saved meals list based on its meal title
        for (MenuVerModel savedMeal : savedMealsList) {
            if (savedMeal.getTv_meal_title().equals(item.getTv_meal_title())) {
                return true;
            }
        }
        return false;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_recipe, iv_save_icon;
        TextView meal_label, calorie, time, meal_title;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            iv_recipe = itemView.findViewById(R.id.iv_recipe);
            meal_label = itemView.findViewById(R.id.tv_meal_label);
            calorie = itemView.findViewById(R.id.tv_calorie);
            time = itemView.findViewById(R.id.tv_time);
            meal_title = itemView.findViewById(R.id.tv_meal_title);
            iv_save_icon = itemView.findViewById(R.id.iv_save_icon);
        }
    }

    public void updateIconState(ImageView saveIcon, MenuVerModel item){
        SharedPreferences sharedPreferences = context.getSharedPreferences("IconState", Context.MODE_PRIVATE);
        boolean isIconSaved = sharedPreferences.getBoolean(ICON_STATE_KEY + item.getTv_meal_title(), false);

        if(isIconSaved){
            saveIcon.setImageResource(R.drawable.saved_icon);
        } else {
            saveIcon.setImageResource(R.drawable.save_icon);
        }
    }
}