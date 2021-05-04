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

public class CreateAccountFragment extends Fragment {

    DataServices.AuthResponse authResponse;
    DataServices.RequestException exception;
    CreateAccountListener accountListener;
    EditText name;
    EditText email;
    EditText password;

    public CreateAccountFragment() {
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
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        name = view.findViewById(R.id.nameRegisterId);
        email = view.findViewById(R.id.emailRegisterId);
        password = view.findViewById(R.id.forumDescription);

        view.findViewById(R.id.submitForumId).setOnClickListener((v) -> {
                new CreateAsyncTask(name.getText().toString(), email.getText().toString(), password.getText().toString()).execute();
            });

        view.findViewById(R.id.cancelNewForumId).setOnClickListener((v) -> {
                accountListener.setLoginUponCancel();
            });
        return view;
    }

    class CreateAsyncTask extends AsyncTask<String, String, Object>{

        String name;
        String email;
        String password;

        public CreateAsyncTask(String name, String email, String password){
            this.name = name;
            this.email = email;
            this.password = password;
        }

        @Override
        protected Object doInBackground(String[] objects) {

            try {
                authResponse = DataServices.register(name, email, password);
            } catch (DataServices.RequestException e) {
                exception = e;
            }
            return authResponse != null? authResponse: exception;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(exception!=null && exception instanceof DataServices.RequestException){
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                accountListener.setForumsFragmentCreate(authResponse);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof CreateAccountListener){
            accountListener = (CreateAccountListener) context;
        }else{
            throw new RuntimeException(getContext()+" should implement this listener.");
        }
    }

    public interface CreateAccountListener{
        void setForumsFragmentCreate(DataServices.AuthResponse authResponse);
        void setLoginUponCancel();
    }
}