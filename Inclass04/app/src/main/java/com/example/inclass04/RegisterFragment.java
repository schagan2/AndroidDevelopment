package com.example.inclass04;

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
                String userName = name.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if(userName == null || userName.isEmpty()){
                    displayToastMessage(R.string.name_warning);
                }else if(userEmail.isEmpty()){
                    displayToastMessage(R.string.email_warning);
                }else if(userPassword.isEmpty()){
                    displayToastMessage(R.string.password_warning);
                }else{
                    DataServices.Account account = DataServices.register(userName, userEmail, userPassword);
                    if(account == null){
                        displayToastMessage(R.string.register_warning);
                    }else{
                        registerListener.setAccount(account);
                    }
                }
            }
        });

        view.findViewById(R.id.cancelId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null){
                    registerListener.cancelRegistration();
                }
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
            throw new RuntimeException(context.toString() + "must implement AccountListener");
        }
    }

    public void displayToastMessage(int message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface RegisterListener{
        void setAccount(DataServices.Account account);
        void cancelRegistration();
    }
}