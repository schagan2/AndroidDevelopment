package com.example.hw04;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    AccountListener accountListener;
    EditText emailText;
    EditText passwordText;
    String uuid;
    String expception;

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
                AsyncTask<String, String, String> msg;
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                msg = new AsyncLogin(email, password).execute();

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

    class AsyncLogin extends AsyncTask<String, String, String> {
        String email;
        String password;

        AsyncLogin(String email, String password){
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                uuid = DataServices.login(email, password);
            } catch (DataServices.RequestException e) {
                expception = e.getMessage();
            }
            return uuid != null? uuid: expception;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (uuid != null){
                accountListener.setAccount(uuid);
            }else{
                Toast.makeText(getContext(), expception, Toast.LENGTH_SHORT).show();
            }
        }
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