package com.example.hw06;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumsFragment extends Fragment {

    private static final String ARG_USER = "user";

    private User user;
    ForumsListener forumsListener;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ForumsAdapter adapter;
    Map<String, User> users = new HashMap();
    ArrayList<Forum> list = new ArrayList();

    public ForumsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment ForumsFragment.
     */
    public static ForumsFragment newInstance(User user) {
        ForumsFragment fragment = new ForumsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forums, container, false);
        getActivity().setTitle(R.string.forums);
        recyclerView = view.findViewById(R.id.recyclerView);
        db = FirebaseFirestore.getInstance();

        db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                Map<String, Object> usr = doc.getData();
                                users.put(doc.getId(), new User(doc.getId(),
                                        (String) usr.get("name"),
                                        (String) usr.get("email"),
                                        (String) usr.get("password")));
                            }
                        }else{
                            createAlertMessage(task.getException().getMessage());
                        }
                    }
                });

        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ForumsAdapter(list, user, getContext());
        recyclerView.setAdapter(adapter);

        getForumsList();
        view.findViewById(R.id.logOutId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forumsListener.goToLoginFragment();
            }
        });

        view.findViewById(R.id.newForumId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: clicked new forum");
                forumsListener.goToNewForum(user);
            }
        });

        return view;
    }

    private void getForumsList() {
        db.collection("forums").get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Forum> l = new ArrayList<>();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                Map<String, Object> map = doc.getData();
                                List<String> likes = (List<String>) doc.get("likedBy");
                                List<User> usersList = new ArrayList<>();
                                for(String s : likes){
                                    usersList.add(users.get(s));
                                }
                                Forum forum = new Forum(doc.getId(),
                                        (String)map.get("title"),
                                        users.get((String)map.get("createdBy")),
                                        ((Timestamp) map.get("date")).toDate(),
                                        (String) map.get("description"),
                                        usersList);
                                l.add(forum);
                            }
                            list.clear();
                            list.addAll(l);
                            adapter.notifyDataSetChanged();
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
        if(context instanceof ForumsListener){
            forumsListener = (ForumsListener) context;
        }else{
            throw new RuntimeException(getContext()+"must implement ForumsFragment.");
        }
    }

    interface ForumsListener{
        void goToForumFragment(Forum forum);
        void goToLoginFragment();
        void goToNewForum(User user);
    }
}