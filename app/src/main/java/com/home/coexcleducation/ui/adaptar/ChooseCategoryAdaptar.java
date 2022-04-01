package com.home.coexcleducation.ui.adaptar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.home.coexcleducation.R;

import java.util.ArrayList;
import java.util.List;

public class ChooseCategoryAdaptar extends ArrayAdapter<String> {

    List<String> categoryList;


    public ChooseCategoryAdaptar(@NonNull Context context, List<String> arrayList) {
        super(context, 0, arrayList);
        categoryList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.category_list, parent, false);
        }

        TextView textView1 = currentItemView.findViewById(R.id.category_name);
        textView1.setText(categoryList.get(position));

        return currentItemView;
    }
}