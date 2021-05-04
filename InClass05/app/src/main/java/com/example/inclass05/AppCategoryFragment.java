package com.example.inclass05;

import android.content.Context;
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

        getActivity().setTitle(R.string.app_category);;
        name = view.findViewById(R.id.welcomeName);
        appList = view.findViewById(R.id.listView);

        DataServices.getAccount(this.token, new DataServices.AccountResponse() {
            @Override
            public void onSuccess(DataServices.Account account) {
                name.setText(getResources().getString(R.string.welcome)+" "+account.getName());
            }

            @Override
            public void onFailure(DataServices.RequestException exception) {
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        DataServices.getAppCategories(this.token, new DataServices.DataResponse<String>() {
            @Override
            public void onSuccess(ArrayList<String> data) {
                categories = (data);
                appAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, categories);
                appList.setAdapter(appAdapter);

                appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        appListener.setAppListFragment(categories.get(position));
                    }
                });
            }

            @Override
            public void onFailure(DataServices.RequestException exception) {
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.logOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appListener.loginAfterLogout();
            }
        });
        return view;
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

    interface AppListener{
        void setAppListFragment(String appCategory);
        void loginAfterLogout();
    }
}