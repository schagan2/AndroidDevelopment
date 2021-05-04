package com.example.hw06;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, RegisterFragment.RegisterListener,
        ForumsFragment.ForumsListener, ForumFragment.ForumListener, NewForumFragment.NewForumListener, ForumsAdapter.AdapterListener {

    FirebaseAuth mauth;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mauth = FirebaseAuth.getInstance();
        if(mauth.getUid() == null || mauth.getCurrentUser() == null){
            Log.d("TAG", "onCreate: "+mauth.getCurrentUser());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerView, new LoginFragment())
                    .commit();
        }
    }

    @Override
    public void createUserAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new RegisterFragment())
                .commit();
    }

    @Override
    public void goToForumsFragment(User user) {
        this.user = user;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, ForumsFragment.newInstance(user))
                .commit();
    }

    @Override
    public void goToLogin() {
        this.user = null;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new LoginFragment())
                .commit();
    }

    @Override
    public void goToForums(User user) {
        goToForumsFragment(user);
    }

    @Override
    public void goToForumFragment(Forum forum) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, ForumFragment.newInstance(this.user, forum))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToLoginFragment() {
        goToLogin();
    }

    @Override
    public void goToNewForum(User user) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, NewForumFragment.newInstance(this.user))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void refreshForum(Forum forum) {
        goToForumFragment(forum);
    }

    @Override
    public void goToForumsFragment() {
        getSupportFragmentManager().popBackStack();
        goToForumsFragment(this.user);
    }

    @Override
    public void refreshForumsAdapter() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, ForumsFragment.newInstance(this.user))
                .commit();
    }

    @Override
    public void goToForumDetail(Forum forum) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, ForumFragment.newInstance(this.user, forum))
                .addToBackStack(null)
                .commit();
    }
}