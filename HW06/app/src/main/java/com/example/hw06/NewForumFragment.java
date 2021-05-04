package com.example.hw06;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewForumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewForumFragment extends Fragment {

    private static final String AUTH_RESPONSE = "auth_response";
    FirebaseFirestore db;
    NewForumListener newForumListener;
    User user;
    EditText title;
    EditText description;

    public NewForumFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment ForumsFragment.
     */
    public static NewForumFragment newInstance(User user) {
        NewForumFragment fragment = new NewForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(AUTH_RESPONSE, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(AUTH_RESPONSE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_forum, container, false);

        getActivity().setTitle(getResources().getString(R.string.new_forum));
        Log.d("TAG", "onCreateView: In New Forum fragment");

        title = view.findViewById(R.id.forumTitle);
        description = view.findViewById(R.id.forumDescription);

        view.findViewById(R.id.cancelNewForumId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newForumListener.goToForumsFragment();
            }
        });

        view.findViewById(R.id.submitForumId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = title.getText().toString();
                String descText = description.getText().toString();

                Map<String, Object> doc = new HashMap();
                doc.put("createdBy", user.getUserId());
                doc.put("description", descText);
                doc.put("title", titleText);
                doc.put("date", new Timestamp(new Date()));

                db.collection("forums").add(doc)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()){
                                    newForumListener.goToForumsFragment();
                                }else{
                                    createAlertMessage(task.getException().getMessage());
                                }
                            }
                        });
            }
        });
        return view;
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
        if(context instanceof NewForumListener){
            newForumListener = (NewForumListener) context;
        }else{
            throw new RuntimeException(getContext()+" must implement NewForumListener.");
        }
    }

    interface NewForumListener{
        void goToForumsFragment();
    }
}