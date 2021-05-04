package com.example.hw03;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppListFragment extends Fragment implements AppRecyclerAdapter.AppRecyclerListener {

    private static final String APP_CATEGORY = "appCategory";
    private static final String TOKEN = "token";

    private String appCategory;
    private String token;

    AppListListener appListListener;
    ArrayList<DataServices.App> apps = new ArrayList<>();
    RecyclerView appsRecyclerView;
    AppRecyclerAdapter appAdapter;
    LinearLayoutManager layoutManager;

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
        appsRecyclerView = view.findViewById(R.id.recyclerAppsList);

        appsRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        appsRecyclerView.setLayoutManager(layoutManager);
        AppRecyclerAdapter.AppRecyclerListener appListener = this;

        DataServices.getAppsByCategory(token, appCategory, new DataServices.DataResponse<DataServices.App>() {
            @Override
            public void onSuccess(ArrayList<DataServices.App> data) {
                apps = (data);
                appAdapter = new AppRecyclerAdapter(apps, appListener);
                appsRecyclerView.setAdapter(appAdapter);
            }

            @Override
            public void onFailure(DataServices.RequestException exception) {
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
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

    @Override
    public void goToAppDetails(DataServices.App app) {
        appListListener.setAppDetails(app);
    }

    interface AppListListener{
        void setAppDetails(DataServices.App app);
    }
}