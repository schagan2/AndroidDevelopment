package com.example.hw04;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hw04.DataServices;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppListFragment extends Fragment {

    private static final String APP_CATEGORY = "appCategory";
    private static final String TOKEN = "token";

    private String appCategory;
    private String token;
    String exception;

    AppListListener appListListener;
    ArrayList<DataServices.App> apps = new ArrayList<>();
    ListView appsList;
    ArrayAdapter appAdapter;

    public AppListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param appCategory Parameter 1
     * @return A new instance of fragment AppListFragment.
     */
    public static AppListFragment newInstance(String token, String appCategory) {
        AppListFragment fragment = new AppListFragment();
        Bundle args = new Bundle();
        args.putString(APP_CATEGORY, appCategory);
        args.putString(TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appCategory = getArguments().getString(APP_CATEGORY);
            token = getArguments().getString(TOKEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_list, container, false);

        getActivity().setTitle(appCategory);
        appsList = view.findViewById(R.id.listViewByCategory);

        new AsyncAppsByCategory(token, appCategory).execute();

        return view;
    }

    class AsyncAppsByCategory extends AsyncTask<String, String, ArrayList<DataServices.App>>{
        ArrayList<DataServices.App> appsByCategory;
        String uuid;
        String appCategory;

        AsyncAppsByCategory(String token, String appCategory){
            this.uuid = token;
            this.appCategory = appCategory;
        }

        @Override
        protected ArrayList<DataServices.App> doInBackground(String... objects) {
            try {
                appsByCategory = DataServices.getAppsByCategory(uuid, appCategory);
            } catch (DataServices.RequestException e) {
                exception = e.getMessage();
            }
            return appsByCategory;
        }

        @Override
        protected void onPostExecute(ArrayList<DataServices.App> apps) {
            super.onPostExecute(apps);
            appAdapter = new AppAdapter(getContext(), R.layout.app_row_item, apps);
            appsList.setAdapter(appAdapter);

            appsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    appListListener.setAppDetails(apps.get(position));
                }
            });
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AppListListener){
            appListListener = (AppListListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement AppListListener");
        }
    }

    interface AppListListener{
        void setAppDetails(DataServices.App app);
    }
}