package com.example.inclass04;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


public class LoginFragment extends Fragment {

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

        emailText = view.findViewById(R.id.emailId);
        passwordText = view.findViewById(R.id.passwordId);

        view.findViewById(R.id.loginButtonId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                if(email == null || email.isEmpty()){
                    Toast.makeText(getContext(), R.string.email_warning, Toast.LENGTH_SHORT).show();
                }else if(password == null || password.isEmpty()){
                    Toast.makeText(getContext(), R.string.password_warning, Toast.LENGTH_SHORT).show();
                }else {
                    DataServices.Account account = DataServices.login(email, password);
                    if (account != null) {
                        accountListener.setAccount(account);
                    } else {
                        Toast.makeText(getContext(), R.string.login_warning, Toast.LENGTH_SHORT).show();
                    }
                }
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
        void setAccount(DataServices.Account account);
        void setRegisterFragment();
    }
}