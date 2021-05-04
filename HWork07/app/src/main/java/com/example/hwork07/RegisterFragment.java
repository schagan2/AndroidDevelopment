package com.example.hwork07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends Fragment {

    RegisterListener registerListener;
    FirebaseAuth mauth;
    FirebaseFirestore db;
    EditText nameRegister, emailRegister, passwordRegister;

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
        getActivity().setTitle(R.string.create_account);
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameRegister = view.findViewById(R.id.nameRegisterId);
        emailRegister = view.findViewById(R.id.emailRegisterId);
        passwordRegister = view.findViewById(R.id.passwordRegisterId);
        Map<String, Object> doc = new HashMap<>();

        view.findViewById(R.id.submitRegisterId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameRegister.getText().toString();
                String email = emailRegister.getText().toString();
                String password = passwordRegister.getText().toString();

                if(email == null || email.isEmpty()){
                    createToastMessage(getResources().getString(R.string.email_error));
                }else if(password == null || password.isEmpty()){
                    createToastMessage(getResources().getString(R.string.password_error));
                }else if(name == null || name.isEmpty()){
                    createToastMessage(getResources().getString(R.string.name_error));
                }else {
                    doc.put("name", name);
                    doc.put("email", email);
                    doc.put("password", password);

                    mauth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        db.collection("profiles").document(mauth.getUid())
                                                .set(doc)
                                                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            registerListener.goToProfilesAfterRegister(new Profile(mauth.getUid(),
                                                                    name,
                                                                    email,
                                                                    password,
                                                                    null));
                                                        }else{
                                                            createToastMessage(task.getException().getMessage());
                                                        }
                                                    }
                                                });
                                    }else{
                                        createToastMessage(task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        });

        view.findViewById(R.id.cancelRegisterId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerListener.goToLogin();
            }
        });
        return view;
    }

    public void createToastMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof RegisterListener){
            registerListener = (RegisterListener) context;
        }else{
            throw new RuntimeException(getContext()+"must implement RegisterListener");
        }
    }

    interface RegisterListener{
        void goToLogin();
        void goToProfilesAfterRegister(Profile profile);
    }
}