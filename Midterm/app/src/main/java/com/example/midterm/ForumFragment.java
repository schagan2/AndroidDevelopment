package com.example.midterm;

import android.content.Context;
import android.os.AsyncTask;
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
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumFragment extends Fragment {

    private static final String ARG_FORUM = "forum";
    private static final String ARG_AUTH = "auth";

    private DataServices.Forum forum;
    ForumFragmentListener forumListener;
    DataServices.AuthResponse authResponse;
    DataServices.Comment comment;
    EditText writeComment;
    TextView title;
    TextView createdBy;
    TextView description;
    TextView noOfComments;
    RecyclerView commentsList;
    CommentAdapter commentAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<DataServices.Comment> comments;
    DataServices.RequestException exception;

    public ForumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @param authResponse
     * @param forum Parameter 1.
     * @return A new instance of fragment ForumFragment.
     */
    public static ForumFragment newInstance(DataServices.AuthResponse authResponse, DataServices.Forum forum) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_AUTH, authResponse);
        args.putSerializable(ARG_FORUM, forum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            authResponse = (DataServices.AuthResponse) getArguments().getSerializable(ARG_AUTH);
            forum = (DataServices.Forum) getArguments().getSerializable(ARG_FORUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forum, container, false);

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

        view.findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommentAddAsyncTask(authResponse.token, forum.getForumId(), writeComment.getText().toString()).execute();
            }
        });
        new ForumDisplayAsyncTask(authResponse.token, forum.getForumId()).execute();

        return view;
    }

    public void refreshComments(String token, long forumId){
        new ForumDisplayAsyncTask(token, forumId);
    }

    class ForumDisplayAsyncTask extends AsyncTask<String, String, Object>{
        String token;
        long forumId;

        public ForumDisplayAsyncTask(String token, long forumId){
            this.token = token;
            this.forumId = forumId;
        }

        @Override
        protected Object doInBackground(String[] objects) {
            try {
                comments = DataServices.getForumComments(token, forumId);
            } catch (DataServices.RequestException e) {
                exception = e;
            }
            return comments!=null && !comments.isEmpty()? comments : exception;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o!=null && o instanceof DataServices.RequestException){
                Toast.makeText(getContext(), ((DataServices.RequestException) o).getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                noOfComments.setText(comments.size()+" Comments");
                commentAdapter = new CommentAdapter(forum, authResponse, comments, forumListener, getContext());
                commentsList.setAdapter(commentAdapter);
            }
        }
    }

    class CommentAddAsyncTask extends AsyncTask<String, String, Object>{
        String token;
        long forumId;
        String text;

        public CommentAddAsyncTask(String token, long forumId, String text){
            this.token = token;
            this.forumId = forumId;
            this.text = text;
        }

        @Override
        protected Object doInBackground(String[] objects) {
            try {
                comment = DataServices.createComment(token, forumId, text);
            } catch (DataServices.RequestException e) {
                exception = e;
            }
            return comment!=null? comment : exception;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o!=null && o instanceof DataServices.RequestException){
                Toast.makeText(getContext(), ((DataServices.RequestException) o).getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                commentAdapter.notifyDataSetChanged();
                new ForumDisplayAsyncTask(authResponse.token, forum.getForumId()).execute();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ForumFragmentListener){
            forumListener = (ForumFragmentListener) context;
        }else{
            throw new RuntimeException(getContext()+" should implement this listener.");
        }
    }

    interface ForumFragmentListener{
        void setForumFragmentAfterDelete(DataServices.Forum forum);
    }
}