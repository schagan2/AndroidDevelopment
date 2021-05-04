package com.example.hwork07;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth mauth;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Profile profile;
    private Photo photo;

    TextView photoTitle;
    ImageView imageView;
    EditText writeComment;
    Button post;
    TextView noOfComments;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    CommentAdapter adapter;
    ArrayList<Comment> commentsList = new ArrayList<>();


    public CommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param profile Parameter 1.
     * @param photo
     * @return A new instance of fragment CommentFragment.
     */
    public static CommentFragment newInstance(Profile profile, Photo photo) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, profile);
        args.putSerializable(ARG_PARAM2, photo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            profile = (Profile) getArguments().getSerializable(ARG_PARAM1);
            photo = (Photo) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        getActivity().setTitle("Comments");
        db = FirebaseFirestore.getInstance();
        mauth = FirebaseAuth.getInstance();
        photoTitle = view.findViewById(R.id.photoTitle);
        noOfComments = view.findViewById(R.id.noOfComments);
        imageView = view.findViewById(R.id.imageView);
        writeComment = view.findViewById(R.id.writeComment);
        recyclerView = view.findViewById(R.id.commentsList);
        post = view.findViewById(R.id.postButton);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CommentAdapter(commentsList, profile, photo, getContext());
        recyclerView.setAdapter(adapter);

        photoTitle.setText(photo.getTitle());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(profile.getProfileId()).child(photo.getPhotoRef().toString());


        Log.d("TAG", "onBindViewHolder: "+storageReference);
        //holder.photo.setImageURI(photo.getPhotoRef());
        Glide.with(getContext())
                .load(storageReference)
                .into(imageView);

        post.setOnClickListener((viewButton) -> {
            String comment = writeComment.getText().toString();
            if(comment != null){
            Map<String, Object> doc = new HashMap<>();
            doc.put("name", mauth.getCurrentUser().getDisplayName());
            doc.put("text", comment);
            doc.put("uid", mauth.getCurrentUser().getUid());

            db.collection("profiles")
                    .document(profile.getProfileId())
                    .collection("photos")
                    .document(photo.getPhotoId())
                    .collection("comments")
                    .add(doc)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "Comment added successfully", Toast.LENGTH_SHORT).show();
                                writeComment.setText("");
                            }else{
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }});

        getComments();
        return view;
    }

    private void getComments() {
        db.collection("profiles")
                .document(profile.getProfileId())
                .collection("photos")
                .document(photo.getPhotoId())
                .collection("comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        commentsList.clear();
                        for(DocumentSnapshot doc : value.getDocuments()){
                            Map<String, Object> map = doc.getData();
                            //String uid, Profile commentUser, String text
                            Comment comment = new Comment(doc.getId(),
                                    (String)map.get("uid"),
                                    (String) map.get("name"),
                                    (String)map.get("text"));
                            commentsList.add(comment);
                        }
                        noOfComments.setText(commentsList.size() +" Comments");
                        photo.setCommentList(commentsList);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}