package com.example.hw04;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hw04.DataServices;
import com.example.hw04.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppCategoryFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TOKEN = "token";

    AppListener appListener;
    ListView appList;
    ArrayAdapter<String> appAdapter;
    TextView name;

    private String token;

    public AppCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param token Parameter 1.
     * @return A new instance of fragment AppCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppCategoryFragment newInstance(String token) {
        AppCategoryFragment fragment = new AppCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    ArrayList<String> categories = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            token = getArguments().getString(ARG_TOKEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_category, container, false);

        name = view.findViewById(R.id.welcomeName);
        appList = view.findViewById(R.id.listView);

        new AsyncAccount(token).execute();

        new AsyncListCategory(token).execute();

        view.findViewById(R.id.logOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appListener.loginAfterLogout();
            }
        });
        return view;
    }

    /*
     * AsyncListCategory extendss the AsyncTask and calls the DataService
     * methods in DoBackground method.
     */
    class AsyncListCategory extends AsyncTask<String, String, ArrayList<String>>{
        String uuid;
        String exception;
        ArrayList<String> categories;

        AsyncListCategory(String uuid){
            this.uuid = uuid;
        }

        @Override
        protected ArrayList<String> doInBackground(String... objects) {
            try {
                categories = DataServices.getAppCategories(token);
            } catch (DataServices.RequestException e) {
                exception = e.getMessage();
            }
            return categories;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            if(strings != null && !strings.isEmpty()){
                appAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, strings);
                appList.setAdapter(appAdapter);

                appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        appListener.setAppListFragment(categories.get(position));
                    }
                });
            }else{
                Toast.makeText(getContext(), exception, Toast.LENGTH_SHORT).show();
            }
        }
    }
    class AsyncAccount extends AsyncTask<String, String, DataServices.Account>{
        String uuid;
        DataServices.Account account;
        String exception;

        AsyncAccount(String uuid){
            this.uuid = uuid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().setTitle(R.string.app_category);
        }

        @Override
        protected DataServices.Account doInBackground(String... s) {
            try {
                account =DataServices.getAccount(uuid);
            } catch (DataServices.RequestException e) {
                exception = e.getMessage();
            }
            return account;
        }

        @Override
        protected void onPostExecute(DataServices.Account account) {
            super.onPostExecute(account);
            if(account != null){
                name.setText(account.getName());
            }else{
                Toast.makeText(getContext(), exception, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AppListener){
            appListener = (AppListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement AppListener");
        }
    }

    public interface AppListener{
        void setAppListFragment(String appCategory);
        void loginAfterLogout();
    }
}