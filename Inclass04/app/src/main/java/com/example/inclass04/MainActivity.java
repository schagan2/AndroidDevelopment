/*

a.Assignment #: InClass 04
b.File Name: InClass04 Main Activity.java
c.Full name of the student: Aakanksha Chauhan, Sindhura Chaganti
Group: C12
 */

package com.example.inclass04;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements LoginFragment.AccountListener, RegisterFragment.RegisterListener, AccountFragment.AccountFragmentListener, UpdateFragment.UpdateListener {

//This method is used to create the Login Fragment for the app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.login);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerView, new LoginFragment())
                .commit();
    }

    //This method allows the user to set up account or profile details
    @Override
    public void setAccount(DataServices.Account account) {
        setTitle(R.string.account);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, AccountFragment.newInstance(account))
                .commit();

    }

    //This method is called when the Cancel button is pressed by the user
    @Override
    public void cancelRegistration() {
        setTitle(R.string.login);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new LoginFragment())
                .commit();
    }

    //This method allows the user to Register in to the app
    @Override
    public void setRegisterFragment() {
        setTitle(R.string.register_account);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new RegisterFragment())
                .commit();
    }

    //This method allows the user to Update the existing profile information
    @Override
    public void setUpdateFragment(DataServices.Account account) {
        setTitle(R.string.update_account);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.containerView, UpdateFragment.newInstance(account))
                .commit();
    }

    //This method allows the user to login to the app
    @Override
    public void afterLogout() {
        setTitle(R.string.login);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new LoginFragment())
                .commit();
    }
// This method is used to update th account fragment and pushes the fragment from the back stack
    @Override
    public void setAccountUpdate(DataServices.Account account) {
        setTitle(R.string.account);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, AccountFragment.newInstance(account))
                .commit();
    }
// The cancel button dismisses the fragment and pops the Account fragment from the back stack
    @Override
    public void cancelUpdate(DataServices.Account account) {
        setTitle(R.string.account);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, AccountFragment.newInstance(account))
                .commit();
    }
}