package com.example.all_menu.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.all_menu.R;
import com.example.all_menu.models.MenuVerModel;

import java.util.List;

public class SelectMealsAdapter extends RecyclerView.Adapter<SelectMealsAdapter.ViewHolder> {

    public void toggleSelection(int position) {
        MenuVerModel item = list.get(position);
        item.setSelected(!item.isSelected());
        notifyItemChanged(position);
    }

    public MenuVerModel getItem(int position) {
        return list.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final Context context;

    private List<MenuVerModel> list;

    private int selectedItemIndex = -1;

    private OnItemClickListener listener;

    public SelectMealsAdapter(Context context, List<MenuVerModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.priced_meal_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuVerModel item = list.get(position);
        holder.bind(item, position);
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_recipe, iv_selected;
        private final TextView meal_label, calorie, time, meal_title;
        private final View selected_overlay_img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_recipe = itemView.findViewById(R.id.iv_recipe);
            meal_label = itemView.findViewById(R.id.tv_meal_label);
            calorie = itemView.findViewById(R.id.tv_calorie);
            time = itemView.findViewById(R.id.tv_time);
            meal_title = itemView.findViewById(R.id.tv_meal_title);
            iv_selected = itemView.findViewById(R.id.iv_selected);
            selected_overlay_img = itemView.findViewById(R.id.selected_overlay_img);
        }

        public void bind(MenuVerModel item, int position) {
            iv_recipe.setImageResource(item.getIv_recipe());
            meal_label.setText(item.getTv_meal_label());
            calorie.setText(item.getTv_calorie());
            time.setText(item.getTv_time());
            meal_title.setText(item.getTv_meal_title());

            // Set visibility based on whether the item is selected or not
            if (position == selectedItemIndex) {
                selected_overlay_img.setVisibility(View.VISIBLE);
                iv_selected.setVisibility(View.VISIBLE);
            } else {
                selected_overlay_img.setVisibility(View.GONE);
                iv_selected.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update selected item index
                    selectedItemIndex = position;
                    // Notify adapter that data has changed to reflect new selection
                    notifyDataSetChanged();
                    // Invoke onItemClick method of the listener
                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });

        }

    }
}
