package com.example.all_menu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.all_menu.R;
import com.example.all_menu.models.MenuVerModel;

import java.util.List;

public class SavedHorAdapter extends RecyclerView.Adapter<SavedHorAdapter.ViewHolder> {

    private List<MenuVerModel> savedMealsList;

    public SavedHorAdapter(List<MenuVerModel> savedMealsList) {
        this.savedMealsList = savedMealsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_hor_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuVerModel meal = savedMealsList.get(position);
        holder.mealImageView.setImageResource(meal.getIv_recipe());
        holder.mealTitleTextView.setText(meal.getTv_meal_title());

    }

    @Override
    public int getItemCount() {
        return savedMealsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mealImageView;
        TextView mealTitleTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mealImageView = itemView.findViewById(R.id.iv_hor_saved_meal);
            mealTitleTextView = itemView.findViewById(R.id.tv_hor_saved_title);
        }
    }
}
