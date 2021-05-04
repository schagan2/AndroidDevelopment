package com.example.hw03;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AppRecyclerAdapter extends RecyclerView.Adapter<AppRecyclerAdapter.AppViewHolder> {

    static ArrayList<DataServices.App> appsList;
    AppRecyclerListener appListener;

    public AppRecyclerAdapter(ArrayList<DataServices.App> apps, AppRecyclerListener appListener){
        this.appListener = appListener;
        this.appsList = apps;
    }

    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_row_item, parent, false);
        return new AppViewHolder(view, appListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        Log.d("TAG", "onBindViewHolder: "+appsList.toString());
        Log.d("TAG", "onBindViewHolder: "+position);
        DataServices.App app = appsList.get(position);
        holder.appName.setText(app.name);
        holder.artistName.setText(app.artistName);
        holder.releaseDate.setText(app.releaseDate);
        holder.app = app;
    }

    @Override
    public int getItemCount() {
        return this.appsList.size();
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        DataServices.App app;
        TextView appName;
        TextView artistName;
        TextView releaseDate;
        View rootView;
        AppRecyclerListener appRecyclerListener;

        public AppViewHolder(View view, AppRecyclerListener appListener) {
            super(view);
            rootView = view;
            appRecyclerListener = appListener;
            appName = (TextView) view.findViewById(R.id.appName);
            artistName = view.findViewById(R.id.artistName);
            releaseDate = view.findViewById(R.id.releaseDate);

            view.setOnClickListener((v) -> {
                appRecyclerListener.goToAppDetails(app);
            });
        }
    }

    interface AppRecyclerListener{
        void goToAppDetails(DataServices.App app);
    }
}
