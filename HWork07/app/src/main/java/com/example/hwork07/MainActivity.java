package com.example.hwork07;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, RegisterFragment.RegisterListener,
        ProfilesListFragment.ProfileListListener, PhotoAdapterListerner, ProfileFragment.ProfileFragmentListener {

    Profile currentUser;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mauth = FirebaseAuth.getInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerView, new LoginFragment())
                    .commit();

    }

    @Override
    public void createUserAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new RegisterFragment())
                .commit();
    }

    @Override
    public void goToProfilesFragment(Profile profile) {
        this.currentUser = profile;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, ProfilesListFragment.newInstance(profile))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new LoginFragment())
                .commit();
    }

    @Override
    public void goToProfilesAfterRegister(Profile profile) {
        this.currentUser = profile;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, ProfilesListFragment.newInstance(profile))
                .commit();
    }

    @Override
    public void goToProfileFragment(Profile profile) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, ProfileFragment.newInstance(profile, this.currentUser))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToLoginAfterLogout() {
        mauth.signOut();
        this.currentUser = null;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new LoginFragment())
                .commit();
    }

    @Override
    public void goToCommentsFragment(Profile profile, Photo photo) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, CommentFragment.newInstance(profile, photo))
                .addToBackStack(null)
                .commit();
    }
}