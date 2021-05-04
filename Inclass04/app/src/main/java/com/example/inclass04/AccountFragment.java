package com.example.inclass04;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ACCOUNT = "account";
    AccountFragmentListener accountFragmentListener;
    
    // Account
    private DataServices.Account account;
    TextView userName;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param account Parameter 1.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(DataServices.Account account) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ACCOUNT, account);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            account = (DataServices.Account) getArguments().getSerializable(ARG_ACCOUNT);
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        userName = view.findViewById(R.id.nameId);
        userName.setText(this.account.getName());

        view.findViewById(R.id.editProfileId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account != null){
                    accountFragmentListener.setUpdateFragment(account);
                }else{
                    Toast.makeText(getContext(), R.string.account_null, Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.findViewById(R.id.logOutId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountFragmentListener.afterLogout();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AccountFragmentListener){
            accountFragmentListener = (AccountFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement AccountListener");
        }
    }

    public interface AccountFragmentListener{
        void setUpdateFragment(DataServices.Account account);
        void afterLogout();
    }
}