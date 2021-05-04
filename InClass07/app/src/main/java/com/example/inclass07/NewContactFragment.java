package com.example.inclass07;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class NewContactFragment extends Fragment {

    NewContactListener newContactListener;
    EditText name;
    EditText email;
    EditText type;
    EditText phone;

    public NewContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_contact, container, false);
        getActivity().setTitle(R.string.create);
        name = view.findViewById(R.id.nameUpdate);
        email = view.findViewById(R.id.emailUpdate);
        phone = view.findViewById(R.id.phoneUpdate);
        type = view.findViewById(R.id.typeUpdate);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        view.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = name.getText().toString();
                String emailString = email.getText().toString();
                String phoneString = phone.getText().toString();
                String typeString = type.getText().toString();

                if(nameString.isEmpty() || emailString.isEmpty() || phoneString.isEmpty() || typeString.isEmpty()){
                    alert.setTitle("Error")
                            .setMessage("All fields are mandatory")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alert.create().show();
                }else{
                    newContactListener.createContact(nameString, emailString, phoneString, typeString);
                }
            }
        });

        view.findViewById(R.id.cancelUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newContactListener.onCancel();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NewContactListener){
            newContactListener = (NewContactListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement NewContactListener");
        }
    }

    interface NewContactListener{
        void createContact(String name, String email, String phone, String type);
        void onCancel();
    }
}