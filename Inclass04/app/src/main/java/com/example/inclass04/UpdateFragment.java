package com.example.inclass04;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private DataServices.Account oldAccount;

    UpdateListener updateListener;
    EditText nameEdit;
    EditText passwordEdit;
    TextView emailText;

    public UpdateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param account Parameter 1.
     * @return A new instance of fragment UpdateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateFragment newInstance(DataServices.Account account) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, account);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            oldAccount = (DataServices.Account) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewUpdate = inflater.inflate(R.layout.fragment_update, container, false);

        nameEdit = viewUpdate.findViewById(R.id.nameUpdateId);
        passwordEdit = viewUpdate.findViewById(R.id.passwordUpdateId);
        emailText = viewUpdate.findViewById(R.id.emailUpdateId);
        emailText.setText(oldAccount.getEmail());
        nameEdit.setText(oldAccount.getName());
        passwordEdit.setText(oldAccount.getPassword());

        viewUpdate.findViewById(R.id.updateButtonId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameEdit == null || nameEdit.getText().toString().isEmpty()){
                    displayMessage(R.string.name_warning);
                }else if(passwordEdit == null || passwordEdit.getText().toString().isEmpty()){
                    displayMessage(R.string.password_warning);
                }else{
                    DataServices.Account updateAccount = DataServices.update(oldAccount, nameEdit.getText().toString(), passwordEdit.getText().toString());
                    updateListener.setAccountUpdate(updateAccount);
                }
            }
        });
        viewUpdate.findViewById(R.id.cancelUpdateId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateListener.cancelUpdate(oldAccount);
            }
        });

        return viewUpdate;
    }

    void displayMessage(int message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof UpdateListener){
            updateListener = (UpdateListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement AccountListener");
        }
    }

    public interface UpdateListener{
        void setAccountUpdate(DataServices.Account account);
        void cancelUpdate(DataServices.Account account);
    }
}