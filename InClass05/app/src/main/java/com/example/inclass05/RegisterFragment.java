package com.example.inclass05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

    RegisterListener registerListener;
    EditText name;
    EditText email;
    EditText password;

    public RegisterFragment() {
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        name = view.findViewById(R.id.nameRegisterId);
        email = view.findViewById(R.id.emailRegisterId);
        password = view.findViewById(R.id.passwordRegisterId);

        view.findViewById(R.id.submitRegisterId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataServices.register(name.getText().toString(), email.getText().toString(), password.getText().toString(),
                        new DataServices.AuthResponse() {
                            @Override
                            public void onSuccess(String token) {
                                registerListener.afterRegistration(token);
                            }

                            @Override
                            public void onFailure(DataServices.RequestException exception) {
                                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        view.findViewById(R.id.cancelId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerListener.setLoginAfterCancel();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof RegisterListener){
            registerListener = (RegisterListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement RegisterListener");
        }
    }

    interface RegisterListener{
        void afterRegistration(String token);

        void setLoginAfterCancel();
    }
}