package com.example.hw03;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppDetailsFragment extends Fragment implements ListAdapter.IListener {

    private static final String APP = "app";

    private DataServices.App app;
    TextView appName;
    TextView artistName;
    TextView releaseDate;
    RecyclerView genreList;
    ListAdapter genreAdapter;
    LinearLayoutManager layoutManager;
    ListAdapter appAdapter;
    ArrayList<String> genres = new ArrayList();

    public AppDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param app Parameter 1.
     * @return A new instance of fragment AppDetailsFragment.
     */
    public static AppDetailsFragment newInstance(DataServices.App app) {
        AppDetailsFragment fragment = new AppDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(APP, app);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            app = (DataServices.App) getArguments().getSerializable(APP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_details, container, false);

        getActivity().setTitle(R.string.app_details);
        appName = view.findViewById(R.id.appDetailName);
        artistName = view.findViewById(R.id.artistDetailName);
        releaseDate = view.findViewById(R.id.releaseDateDetail);
        genreList = view.findViewById(R.id.genresRecyclerView);

        appName.setText(app.name);
        artistName.setText(app.artistName);
        releaseDate.setText(app.releaseDate);

        genreList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        genreList.setLayoutManager(layoutManager);

        genreAdapter = new ListAdapter(app.genres, this);
        genreList.setAdapter(genreAdapter);
        return view;
    }

    @Override
    public void setAppListFragment(String appCategory) {
        //Nothing to do
    }
}