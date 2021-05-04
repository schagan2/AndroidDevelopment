package com.example.inclass07;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    ContactListFragment.ContactListListener contactListListener;
    ArrayList<Contact> contacts;
    TextView name;
    TextView email;
    TextView phone;
    TextView type;

    public ContactAdapter(ArrayList<Contact> contactsList, ContactListFragment.ContactListListener contactListListener){
        this.contacts = contactsList;
        this.contactListListener = contactListListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_item, parent, false);
        ContactViewHolder viewHolder = new ContactViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.name.setText(contact.getName());
        holder.email.setText(contact.getEmail());
        holder.phone.setText(contact.getPhone());
        holder.type.setText(contact.getType());
        holder.position = position;
        holder.contacts = contacts;
        holder.listener = contactListListener;
    }

    @Override
    public int getItemCount() {
        return this.contacts.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{
        ContactListFragment.ContactListListener listener;
        TextView name;
        TextView email;
        TextView phone;
        TextView type;
        int position;
        ArrayList<Contact> contacts;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameText);
            email = itemView.findViewById(R.id.emailText);
            phone = itemView.findViewById(R.id.phoneText);
            type = itemView.findViewById(R.id.typeText);

            itemView.setOnClickListener((v) ->
                    listener.setDetailsFragment(contacts.get(position)));

            itemView.findViewById(R.id.deleteButton).setOnClickListener((v) ->
                    listener.deleteContact(contacts.get(position).getId()));
        }
    }
}
