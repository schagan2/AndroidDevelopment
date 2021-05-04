package com.example.inclass07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CONTACT = "contact";

    UpdateListener updateListener;
    private Contact contact;
    EditText name;
    EditText email;
    EditText type;
    EditText phone;
    String nameVal;
    String emailVal;
    String phoneVal;
    String typeVal;

    public UpdateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param contact Parameter 1.
     * @return A new instance of fragment UpdateFragment.
     */
    public static UpdateFragment newInstance(Contact contact) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contact = (Contact) getArguments().getSerializable(ARG_CONTACT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        getActivity().setTitle(R.string.update_details);
        name = view.findViewById(R.id.nameUpdate);
        email = view.findViewById(R.id.emailUpdate);
        phone = view.findViewById(R.id.phoneUpdate);
        type = view.findViewById(R.id.typeUpdate);

        name.setText(contact.getName());
        email.setText(contact.getEmail());
        phone.setText(contact.getPhone());
        type.setText(contact.getType());

        view.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameVal = name.getText().toString();
                emailVal = email.getText().toString();
                phoneVal = phone.getText().toString();
                typeVal = type.getText().toString();

                if(nameVal.isEmpty() || emailVal.isEmpty() || phoneVal.isEmpty() || typeVal.isEmpty()){
                    Log.d("TAG", "onClick: All are empty");
                }else {
                    contact.setName(nameVal);
                    contact.setEmail(emailVal);
                    contact.setPhone(phoneVal);
                    contact.setType(typeVal);
                    updateListener.updateContact(contact);
                }
            }
        });

        view.findViewById(R.id.cancelUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateListener.setDetailsFragmentAfterCancel(contact);
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof UpdateListener){
            updateListener = (UpdateListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement DetailsListener");
        }
    }

    interface UpdateListener{
        void updateContact(Contact contact);
        void setDetailsFragmentAfterCancel(Contact contact);
    }
}