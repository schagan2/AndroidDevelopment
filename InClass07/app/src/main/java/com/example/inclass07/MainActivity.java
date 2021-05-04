package com.example.inclass07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements ContactListFragment.ContactListListener,
        NewContactFragment.NewContactListener, DetailsFragment.DetailsListener, UpdateFragment.UpdateListener {

    private final OkHttpClient client = new OkHttpClient();
    private static final String NEW_CONTACT = "NewContact";
    private static final String CONTACT_LIST = "ContactList";
    private static final String DETAIL_FRAGEMENT = "Detail_fragment";
    ArrayList<Contact> contactList;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getContacts();
    }

    void getContacts(){
        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/contacts")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String res = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fragment = (NewContactFragment) getSupportFragmentManager().findFragmentByTag(NEW_CONTACT);
                            fragment = fragment==null?getSupportFragmentManager().findFragmentByTag(CONTACT_LIST):fragment;
                            fragment = fragment==null?getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGEMENT):fragment;
                            if(fragment == null) {
                                Log.d("TAG", "onResponse: "+ res.length());
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.containerView, ContactListFragment.newInstance(res), "ContactList")
                                        .commit();
                            }else{
                                Log.d("TAG", "onResponse: "+ res.length());
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.containerView, ContactListFragment.newInstance(res), "ContactList")
                                        .commit();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void setCreateContact() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new NewContactFragment(), NEW_CONTACT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void createContact(String name, String email, String phone, String type) {
        FormBody body = new FormBody.Builder()
                .add("name", name)
                .add("email", email)
                .add("phone", phone)
                .add("type", type)
                .build();

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/contact/create")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    getContacts();
                }
            }
        });
    }

    @Override
    public void onCancel() {
        getContacts();
    }

    @Override
    public void setDetailsFragment(Contact contact) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, DetailsFragment.newInstance(contact), DETAIL_FRAGEMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void deleteContact(String contactId) {
        FormBody body = new FormBody.Builder()
                .add("id", contactId)
                .build();

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/contact/delete")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    getContacts();
                }
            }
        });
    }

    @Override
    public void setUpdateFragment(Contact contact) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, UpdateFragment.newInstance(contact))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void updateContact(Contact contact) {
        FormBody body = new FormBody.Builder()
                .add("name", contact.getName())
                .add("email", contact.getEmail())
                .add("phone", contact.getPhone())
                .add("type", contact.getType())
                .build();

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/contact/update")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(() -> {
                    setDetailsFragment(contact);
                });

            }
        });
    }

    @Override
    public void setDetailsFragmentAfterCancel(Contact contact) {
        setDetailsFragment(contact);
    }
}