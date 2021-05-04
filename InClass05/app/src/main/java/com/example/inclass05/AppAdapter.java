package com.example.inclass05;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AppAdapter extends ArrayAdapter {

    TextView appName;
    TextView artistName;
    TextView releaseDate;

    public AppAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.app_row_item, parent, false);
        }

        DataServices.App app = (DataServices.App) getItem(position);
        appName = convertView.findViewById(R.id.appName);
        artistName = convertView.findViewById(R.id.artistName);
        releaseDate = convertView.findViewById(R.id.releaseDate);

        appName.setText(app.name);
        artistName.setText(app.artistName);
        releaseDate.setText(app.releaseDate);

        return convertView;
    }
}
