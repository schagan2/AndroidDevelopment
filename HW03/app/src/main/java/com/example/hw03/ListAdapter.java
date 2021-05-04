package com.example.hw03;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.CookieHandler;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>{

    static ArrayList<String> list;
    IListener listener;

    public ListAdapter(ArrayList<String> list, IListener listener){
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ListViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            holder.category.setText(list.get(position));
            holder.position = position;
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView category;
        int position;

        public ListViewHolder(View view, IListener listener) {
            super(view);
            category = view.findViewById(android.R.id.text1);

            view.setOnClickListener((v) -> {
                listener.setAppListFragment(list.get(position));
            });
        }
    }

    interface IListener {
        void setAppListFragment(String appCategory);
    }
}
