package com.example.hw06;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class LoginFragment extends Fragment {

    private final static String TAG = "loginFragment";
    LoginListener loginListener;
    FirebaseAuth mauth;
    FirebaseFirestore db;
    EditText email, password;
    User user;

    public LoginFragment() {
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

        getActivity().setTitle(R.string.login);
        email = view.findViewById(R.id.emailId);
        password = view.findViewById(R.id.passwordId);
        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        view.findViewById(R.id.loginButtonId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = email.getText().toString();
                String passwordInput = password.getText().toString();

                if(emailInput == null || emailInput.isEmpty()){
                    createAlertMessage(getResources().getString(R.string.email_error));
                }else if(passwordInput == null || passwordInput.isEmpty()){
                    createAlertMessage(getResources().getString(R.string.password_error));
                }else{
                    mauth.signInWithEmailAndPassword(emailInput, passwordInput)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        goToForums();
                                    }else{
                                        createAlertMessage(task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        });

        view.findViewById(R.id.createAccountId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginListener.createUserAccount();
            }
        });

        return view;
    }

    private void goToForums() {
        db.collection("users").get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                if(doc.getData().get("email").equals(mauth.getCurrentUser().getEmail())){
                                    user = new User(doc.getId(),
                                            (String) doc.get("name"),
                                            (String) doc.get("email"),
                                            (String) doc.get("password"));
                                    loginListener.goToForumsFragment(user);
                                }
                            }
                        }else{
                            createAlertMessage(task.getException().getMessage());
                        }
                    }
                });
    }

    public void createAlertMessage(String msg){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Error").setMessage(msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LoginListener){
            loginListener = (LoginListener) context;
        }else{
            throw new RuntimeException(getContext()+"must implement LoginListener");
        }
    }

    interface LoginListener{
        void createUserAccount();
        void goToForumsFragment(User user);
    }
}