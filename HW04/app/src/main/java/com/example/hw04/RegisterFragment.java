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

public class RegisterFragment extends Fragment {

    RegisterListener registerListener;
    EditText name;
    EditText email;
    EditText password;
    String exception;
    String token;

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
                new AsyncRegister().execute();
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

    class AsyncRegister extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... objects) {
            try {
                token = DataServices.register(name.getText().toString(), email.getText().toString(), password.getText().toString());
            } catch (DataServices.RequestException e) {
                exception = e.getMessage();
            }
            return token;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            if(string != null){
                registerListener.afterRegistration(string);
            }else{
                Toast.makeText(getContext(), exception, Toast.LENGTH_SHORT).show();
            }
        }
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