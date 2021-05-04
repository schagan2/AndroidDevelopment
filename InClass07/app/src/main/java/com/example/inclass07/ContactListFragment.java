package com.example.inclass07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactListFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CONTACTS = "contacts";

    String contactsString;
    ArrayList<Contact> contacts = new ArrayList<>();
    RecyclerView listView;
    LinearLayoutManager layoutManager;
    ContactAdapter adapter;
    ContactListListener contactListener;

    public ContactListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param contactString Parameter 1.
     * @return A new instance of fragment ContactListFragment.
     */
    public static ContactListFragment newInstance(String contactString) {
        ContactListFragment fragment = new ContactListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONTACTS, contactString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contactsString = getArguments().getString(ARG_CONTACTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        getActivity().setTitle(R.string.contacts);
        String[] contactString = contactsString.split("\n");

        for (String contact: contactString) {
            String[] contactValues = contact.split(",");
            Contact contactObj = new Contact(contactValues[0], contactValues[1], contactValues[2], contactValues[3], contactValues[4]);
            contacts.add(contactObj);
        }
        
        listView = view.findViewById(R.id.listView);

        layoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(layoutManager);

        adapter = new ContactAdapter(contacts, contactListener);
        listView.setAdapter(adapter);

        view.findViewById(R.id.createContactButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactListener.setCreateContact();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ContactListListener){
            contactListener = (ContactListListener) context;
        }else{
            Log.d("TAG", "onAttach: "+ "must implement the listener");
        }
    }

    interface ContactListListener{
        void setDetailsFragment(Contact contact);
        void deleteContact(String contactId);
        void setCreateContact();
    }
}