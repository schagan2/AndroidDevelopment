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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumsFragment extends Fragment {

    private static final String AUTH_RESPONSE = "authResponse";

    DataServices.AuthResponse authResponse;
    DataServices.RequestException exception;
    ForumListener forumListener;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ForumAdapter forumAdapter;
    ArrayList<DataServices.Forum> forums;

    public ForumsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param authResponse Parameter 1.
     * @return A new instance of fragment ForumsFragment.
     */
    public static ForumsFragment newInstance(DataServices.AuthResponse authResponse) {
        ForumsFragment fragment = new ForumsFragment();
        Bundle args = new Bundle();
        args.putSerializable(AUTH_RESPONSE, authResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            authResponse = (DataServices.AuthResponse) getArguments().getSerializable(AUTH_RESPONSE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forums, container, false);

        view.findViewById(R.id.logOutId).setOnClickListener((v) -> {
            forumListener.setLoginUponLogout();
        });

        view.findViewById(R.id.newForumId).setOnClickListener((v) -> {
            forumListener.setNewForum();
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        new ForumAsyncTask(authResponse.token).execute();

        return view;
    }

    class ForumAsyncTask extends AsyncTask<String, String, Object>{
        String token;

        public ForumAsyncTask(String token){
            this.token = token;
        }

        @Override
        protected Object doInBackground(String[] objects) {
            try {
                forums = DataServices.getAllForums(token);
            } catch (DataServices.RequestException e) {
                exception = e;
            }
            return forums!=null || !forums.isEmpty()? forums: exception;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(exception != null && exception instanceof DataServices.RequestException){
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                forumAdapter = new ForumAdapter(forums, authResponse, forumListener);
                recyclerView.setAdapter(forumAdapter);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ForumListener){
            forumListener = (ForumListener) context;
        }else{
            throw new RuntimeException(getContext()+" should implement this listener.");
        }
    }

    public interface ForumListener{
        void setLoginUponLogout();
        void setNewForum();
        void setForumsFragmentAfterDelete();
        void setForumFragment(DataServices.Forum forum);
    }
}