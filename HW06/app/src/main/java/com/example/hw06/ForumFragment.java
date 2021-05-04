package com.example.hw06;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ForumFragment extends Fragment {

    private static final String ARG_FORUM = "forum";
    private static final String ARG_AUTH = "auth";

    private Forum forum;
    private User user;
    ForumListener forumListener;
    FirebaseFirestore db;

    Comment comment;
    EditText writeComment;
    TextView title;
    TextView createdBy;
    TextView description;
    TextView noOfComments;
    RecyclerView commentsList;
    CommentAdapter commentAdapter;
    LinearLayoutManager linearLayoutManager;
    User usr;
    ArrayList<Comment> comments = new ArrayList<>();

    public ForumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @param user
     * @param forum Parameter 1.
     * @return A new instance of fragment ForumFragment.
     */
    public static ForumFragment newInstance(User user, Forum forum) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_AUTH, user);
        args.putSerializable(ARG_FORUM, forum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_AUTH);
            forum = (Forum) getArguments().getSerializable(ARG_FORUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        getActivity().setTitle(R.string.forum);
        db = FirebaseFirestore.getInstance();
        title = view.findViewById(R.id.forumTitleDisplay);
        createdBy = view.findViewById(R.id.createdByDisplay);
        description = view.findViewById(R.id.descriptionDisplay);
        noOfComments = view.findViewById(R.id.commentsCount);
        commentsList = view.findViewById(R.id.commentsList);
        writeComment = view.findViewById(R.id.writeComment);

        title.setText(forum.getTitle());
        createdBy.setText(forum.getCreatedBy().getName());
        description.setText(forum.getDescription());

        commentsList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        commentsList.setLayoutManager(linearLayoutManager);

        db.collection("comments").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                Map<String, Object> obj = doc.getData();
                                if(obj.get("forumId").equals(forum.getForumId())){
                                    //String text, User createdBy, Date createdAt, String commentId
                                    db.collection("users").document((String)obj.get("createdBy")).get()
                                            .addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    DocumentSnapshot dsUser = task.getResult();
                                                    if(task.isSuccessful()){
                                                        usr = new User(dsUser.getId(),
                                                                (String) dsUser.get("name"),
                                                                (String) dsUser.get("email"),
                                                                (String) dsUser.get("password"));

                                                    }else{
                                                        createAlertMessage(task.getException().getMessage());
                                                    }
                                                }
                                            });
                                    Comment comment = new Comment((String) obj.get("text"),
                                            usr,
                                            ((Timestamp)obj.get("createdAt")).toDate(),
                                            doc.getId());
                                    comments.add(comment);
                                    commentAdapter.notifyDataSetChanged();
                                }
                            }
                        }else{
                            createAlertMessage(task.getException().getMessage());
                        }
                    }
                });

        view.findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap();
                map.put("forumId", forum.getForumId());
                map.put("createdBy", user.getUserId());
                map.put("createdAt", new Timestamp(new Date(System.currentTimeMillis())));
                map.put("text", writeComment.getText().toString());
                db.collection("comments")
                        .add(map)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                refreshComments();
                            }
                        });
            }
        });

        noOfComments.setText(comments.size()+" Comments");
        commentAdapter = new CommentAdapter(forum, user, comments, new ForumFragment());
        commentsList.setAdapter(commentAdapter);
        return view;
    }

    public void refreshComments(){
        forumListener.refreshForum(forum);
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
        if(context instanceof ForumListener){
            forumListener = (ForumListener) context;
        }else{
            throw new RuntimeException(getContext()+" must implement ForumListener.");
        }
    }

    interface ForumListener{
        void refreshForum(Forum forum);
    }
}