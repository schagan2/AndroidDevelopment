package com.example.hwork07;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginFragment extends Fragment {

    LoginListener loginListener;
    FirebaseAuth mauth;
    FirebaseFirestore db;
    EditText email, password;
    Profile profile;
    List<Photo> photoList;

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
                    createToastMessage(getResources().getString(R.string.email_error));
                }else if(passwordInput == null || passwordInput.isEmpty()){
                    createToastMessage(getResources().getString(R.string.password_error));
                }else{
                    mauth.signInWithEmailAndPassword(emailInput, passwordInput)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        getProfileUser();
                                    }else{
                                        createToastMessage(task.getException().getMessage());
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

    private void getProfileUser() {
        db.collection("profiles").document(mauth.getUid())
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Map<String, Object> doc = task.getResult().getData();
                            //String profileId, String name, String email, String password, List<Photo> photoRefs
                            profile = new Profile(task.getResult().getId(),
                                    (String) doc.get("name"),
                                    (String) doc.get("email"),
                                    (String) doc.get("password"),
                                    null);
                            loginListener.goToProfilesFragment(profile);
                        }else{
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*private void getPhotoRefs(){
        photoList.clear();
        db.collection("profiles")
                .document(id)
                .collection("photos")
                .get().addOnCompleteListener((task) -> {
            if(task.isSuccessful()){
                List<Photo> photos = new ArrayList<>();
                for(DocumentSnapshot doc : task.getResult().getDocuments()){
                    Map<String, Object> photoMap = doc.getData();
                    //String photoId, List<Comment> commentList, List<User> likedBy, String photoRef, String title

                    List<User> likedBy = new ArrayList<>();
                    for (User user : allUsers) {
                        List<String> photoLiked = (List<String>) photoMap.get("likedBy");
                        if(photoLiked.contains(user.getUserId())){
                            likedBy.add(user);
                        }
                    }

                    Log.d("TAG", "getPhotoRefs: "+likedBy.toString());
                    List<Comment> comments = new ArrayList<>();
                    db.collection("profiles")
                            .document(id)
                            .collection("photos")
                            .document(doc.getId())
                            .collection("comments")
                            .get()
                            .addOnCompleteListener((taskComment) -> {
                                if(taskComment.isSuccessful()){
                                    List<Comment> comm = new ArrayList<>();
                                    for(DocumentSnapshot docComm : taskComment.getResult().getDocuments()){
                                        Map<String, Object> commentMap = docComm.getData();
                                        //String uid, User commentUser, String text
                                        comm.add(new Comment((String)commentMap.get("uid"),
                                                getUser((String)commentMap.get("uid")),
                                                (String)commentMap.get("text")));
                                        Log.d("TAG", "getPhotoRefs: "+comments);
                                    }
                                    comments.addAll(comm);
                                }
                            });

                    Photo pho = new Photo(doc.getId(),
                            comments,
                            likedBy,
                            Uri.parse((String)photoMap.get("photoRef")),
                            (String)photoMap.get("title"));
                    //setPhotoRefs(pho);
                    photos.add(pho);
                    //pRef.add(pho);
                    Log.d("TAG", "getPhotoRefs: "+pho.toString()+", "+photos.size());
                }
                photoRefs.clear();
                photoRefs.addAll(photos);
            }
        });
        Log.d("TAG", "getPhotoRefs: "+ photoRefs.size());
        return photoRefs;
    }*/

    public void createToastMessage(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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
        void goToProfilesFragment(Profile profile);
    }
}