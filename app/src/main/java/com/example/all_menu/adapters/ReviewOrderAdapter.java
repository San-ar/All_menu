package com.example.all_menu.adapters;

import android.app.Activity;
import android.content.Context;
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

public class ReviewOrderAdapter extends RecyclerView.Adapter<ReviewOrderAdapter.ViewHolder> {

    private static List<MenuVerModel> mealList;
    private Context context;
    private QuantityChangeListener listener;

    public ReviewOrderAdapter(List<MenuVerModel> mealList, Context context, QuantityChangeListener listener) {
        this.mealList = mealList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        return new ViewHolder(view, listener);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuVerModel meal = mealList.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public interface QuantityChangeListener {
        void onQuantityChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivReviewMeal;
        private final TextView tvReviewTitle;
        private final TextView reviewPrice;
        private final TextView reviewQuantity;
        private final TextView minusTextView;
        private final TextView plusTextView;
        private MenuVerModel meal;

        private QuantityChangeListener listener;

        public ViewHolder(@NonNull View itemView, QuantityChangeListener listener) {
            super(itemView);
            ivReviewMeal = itemView.findViewById(R.id.iv_review_meal);
            tvReviewTitle = itemView.findViewById(R.id.tv_review_title);
            reviewPrice = itemView.findViewById(R.id.review_price);
            reviewQuantity = itemView.findViewById(R.id.review_qt);
            minusTextView = itemView.findViewById(R.id.minus_qt);
            plusTextView = itemView.findViewById(R.id.add_qt);
            this.listener = listener;

            minusTextView.setOnClickListener(v -> {
                if (meal != null) {
                    int quantity = meal.getQuantity();
                    if (quantity > 1) {
                        quantity--;
                        meal.setQuantity(quantity);
                        reviewQuantity.setText(String.valueOf(quantity));
                        updateIndividualPrice(); // Update individual price
                        listener.onQuantityChanged(); // Notify activity of quantity change
                    }
                }
            });

            plusTextView.setOnClickListener(v -> {
                if (meal != null) {
                    int quantity = meal.getQuantity();
                    quantity++;
                    meal.setQuantity(quantity);
                    reviewQuantity.setText(String.valueOf(quantity));
                    updateIndividualPrice(); // Update individual price
                    listener.onQuantityChanged(); // Notify activity of quantity change
                }
            });
        }

        public void bind(MenuVerModel meal) {
            this.meal = meal;
            ivReviewMeal.setImageResource(meal.getIv_recipe());
            tvReviewTitle.setText(meal.getTv_meal_title());
            reviewQuantity.setText(String.valueOf(meal.getQuantity()));
            updateIndividualPrice(); // Update individual price
        }

        private void updateIndividualPrice() {
            int quantity = meal.getQuantity();
            double individualPrice = 20.00 * quantity; // Assuming each meal costs 20.00
            reviewPrice.setText(String.format("%.2f", individualPrice));
        }
    }



}

