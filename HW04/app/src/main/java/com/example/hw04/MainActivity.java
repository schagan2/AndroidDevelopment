package com.example.hw04;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.hw04.AppCategoryFragment;

import java.nio.channels.AsynchronousChannelGroup;

/*
* Assignment - HW04
* File name: MainActivity.java[HW04.app]
* Name: Aakansha Chauhan, Sindhura Chaganti
*/
public class MainActivity extends AppCompatActivity implements LoginFragment.AccountListener, RegisterFragment.RegisterListener,
        AppCategoryFragment.AppListener, AppListFragment.AppListListener {

    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.login);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerView, new LoginFragment())
                .commit();
    }

    @Override
    public void setAccount(String accountId) {
        //When successful login, goes to app category fragment
        setTitle(R.string.app_category);
        token = accountId;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, AppCategoryFragment.newInstance(accountId))
                .commit();
    }

    @Override
    public void setRegisterFragment() {
        //Will open register page
        setTitle(R.string.register_account);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new RegisterFragment())
                .commit();
    }

    @Override
    public void setAppListFragment(String appCategory) {
        //Will direct to app list page after selecting the category
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, AppListFragment.newInstance(token, appCategory))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void loginAfterLogout() {
        //will open login page after logout
        setTitle(R.string.login);
        token = null;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new LoginFragment())
                .commit();
    }

    @Override
    public void setAppDetails(DataServices.App app) {
        //Goes to app details page
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, AppDetailsFragment.newInstance(app))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void afterRegistration(String uuid) {
        //Will open app category page after successful registration
        token = uuid;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, AppCategoryFragment.newInstance(uuid))
                .commit();
    }

    @Override
    public void setLoginAfterCancel() {
        //After clicking cancel on register fragment, should return to login page
        setTitle(R.string.login);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new LoginFragment())
                .commit();
    }
}