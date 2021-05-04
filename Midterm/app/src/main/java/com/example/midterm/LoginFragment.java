package com.example.midterm;

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

    DataServices.AuthResponse authResponse;
    DataServices.RequestException exception;
    LoginListener loginListener;
    EditText email;
    EditText password;

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
        email = view.findViewById(R.id.emailId);
        password =  view.findViewById(R.id.passwordId);

        view.findViewById(R.id.loginButtonId).setOnClickListener((v) -> {
                new LoginAsynTask(email.getText().toString(), password.getText().toString()).execute();
            });

        view.findViewById(R.id.createAccountId).setOnClickListener((v) -> {
                loginListener.setCreateFragment();
            });
        return view;
    }

    class LoginAsynTask extends AsyncTask<String, String, Object>{
        DataServices.AuthResponse auth;
        String email;
        String password;

        public LoginAsynTask(String email, String password){
            this.email = email;
            this.password = password;
        }

        @Override
        protected Object doInBackground(String[] objects) {
            try {
                auth = DataServices.login(email, password);
            } catch (DataServices.RequestException e) {
                exception = e;
            }
            return auth!=null? auth: exception;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o instanceof DataServices.RequestException){
                Toast.makeText(getContext(), ((DataServices.RequestException) o).getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                loginListener.setForumsFragment((DataServices.AuthResponse) o);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LoginListener){
            loginListener = (LoginListener) context;
        }else{
            throw new RuntimeException(getContext()+" should implement this listener.");
        }
    }

    public interface LoginListener{
        void setForumsFragment(DataServices.AuthResponse authResponse);
        void setCreateFragment();
    }
}