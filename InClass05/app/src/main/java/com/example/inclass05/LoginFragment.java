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

public class LoginFragment extends Fragment {

    DataServices.AuthResponse authCallBack;
    AccountListener accountListener;
    EditText emailText;
    EditText passwordText;

    public LoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Getting Text views from fragment_login.xml
        emailText = view.findViewById(R.id.emailId);
        passwordText = view.findViewById(R.id.passwordId);

        view.findViewById(R.id.loginButtonId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                DataServices.login(email, password, new DataServices.AuthResponse() {
                    @Override
                    public void onSuccess(String token) {
                        accountListener.setAccount(token);
                    }

                    @Override
                    public void onFailure(DataServices.RequestException exception) {
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        view.findViewById(R.id.createAccountId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountListener.setRegisterFragment();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AccountListener){
            accountListener = (AccountListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement AccountListener");
        }
    }

    public interface AccountListener{
        void setAccount(String accountId);
        void setRegisterFragment();
    }
}