package com.example.midterm;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.content.AsyncTaskLoader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class NewForumFragment extends Fragment {

    private static final String AUTH_RESPONSE = "auth_response";
    DataServices.AuthResponse authResponse;
    NewForumListener newForumListener;
    DataServices.RequestException exception;
    DataServices.Forum forum;
    EditText title;
    EditText description;

    public NewForumFragment() {
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
        View view = inflater.inflate(R.layout.fragment_new_forum, container, false);

        title = view.findViewById(R.id.forumTitle);
        description = view.findViewById(R.id.forumDescription);

        view.findViewById(R.id.cancelNewForumId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newForumListener.setForumUponCancel();
            }
        });

        view.findViewById(R.id.submitForumId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NewForumAsyncTask(authResponse.token, title.getText().toString(), description.getText().toString()).execute();
            }
        });
        return view;
    }

    class NewForumAsyncTask extends AsyncTask<String, String, Object> {
        String token;
        String title;
        String description;

        public NewForumAsyncTask(String token, String title, String description){
            this.token = token;
            this.title = title;
            this.description = description;
        }

        @Override
        protected Object doInBackground(String[] objects) {
            try {
                forum = DataServices.createForum(token, title, description);
            } catch (DataServices.RequestException e) {
                exception = e;
            }
            return forum!=null ? forum : exception;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o!=null && o instanceof DataServices.RequestException){
                Toast.makeText(getContext(), ((DataServices.RequestException) o).getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                newForumListener.setForumUponCancel();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof NewForumListener){
            newForumListener = (NewForumListener) context;
        }else{
            throw new RuntimeException(getContext()+" should implement this listener.");
        }
    }

    interface NewForumListener{
        void setForumUponCancel();
    }
}