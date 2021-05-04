package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

/*
 * Assignment: Midterm
 * File name: MianActivity.java[Midterm.app]
 * Name: Sindhura Chaganti
 */
public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, CreateAccountFragment.CreateAccountListener,
        ForumsFragment.ForumListener, NewForumFragment.NewForumListener, ForumFragment.ForumFragmentListener {

    DataServices.AuthResponse authResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerView, new LoginFragment())
                .commit();
    }

    @Override
    public void setForumsFragment(DataServices.AuthResponse authResponse) {
        setTitle(R.string.forums);
        this.authResponse = authResponse;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, ForumsFragment.newInstance(authResponse))
                .commit();
    }

    @Override
    public void setForumsFragmentCreate(DataServices.AuthResponse authResponse) {
        setForumsFragment(authResponse);
    }

    @Override
    public void setLoginUponCancel() {
        setTitle(R.string.login);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new LoginFragment())
                .commit();
    }

    @Override
    public void setCreateFragment() {
        setTitle(R.string.create_account);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new CreateAccountFragment())
                .commit();
    }

    @Override
    public void setLoginUponLogout() {
        setLoginUponCancel();
    }

    @Override
    public void setNewForum() {
        setTitle(R.string.create_forum);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, NewForumFragment.newInstance(authResponse))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setForumsFragmentAfterDelete() {
        setForumsFragment(authResponse);
    }

    @Override
    public void setForumFragment(DataServices.Forum forum) {
        setTitle(R.string.forum);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, ForumFragment.newInstance(authResponse, forum))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setForumUponCancel() {
        getSupportFragmentManager().popBackStack();
        setForumsFragment(authResponse);
    }

    @Override
    public void setForumFragmentAfterDelete(DataServices.Forum forum) {
        setForumFragment(forum);
    }
}