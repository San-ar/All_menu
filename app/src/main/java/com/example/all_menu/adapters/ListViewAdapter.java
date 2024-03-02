package com.example.all_menu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.all_menu.R;
import com.example.all_menu.models.ListViewModel;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<ListViewModel> {

    public ListViewAdapter(Context context, int resource, List<ListViewModel> itemList){

        super(context, resource, itemList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ListViewModel listViewModel = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.week_list_item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.tv_list_item_label);

        textView.setText(listViewModel.getLabel());

        return convertView;

    }
}
