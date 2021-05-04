package com.example.hwork07;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilesListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final int PICK_IMAGE = 100;

    private Profile currentProfile;
    Uri imageUri;
    TextView profileName;
    ListView listView;
    ArrayAdapter<Profile> adapter;
    FirebaseFirestore db;
    FirebaseAuth mauth;
    ProfileListListener profileListListener;
    List<Profile> allUsers = new ArrayList<>();
    ArrayList<Profile> profiles = new ArrayList<>();

    public ProfilesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param profile Parameter 1.
     * @return A new instance of fragment ProfilesListFragment.
     */
    public static ProfilesListFragment newInstance(Profile profile) {
        ProfilesListFragment fragment = new ProfilesListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, profile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentProfile = (Profile) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profiles_list, container, false);
        getActivity().setTitle("Profiles");
        db = FirebaseFirestore.getInstance();
        mauth = FirebaseAuth.getInstance();

        profileName = view.findViewById(R.id.profileName);
        profileName.setText(currentProfile.getName());

        listView = view.findViewById(R.id.profilesListView);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, profiles);
        listView.setAdapter(adapter);

        getAllProfiles();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                profileListListener.goToProfileFragment(profiles.get(position));
            }
        });
        view.findViewById(R.id.addProfileButton).setOnClickListener((viewAdd) -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        });

        view.findViewById(R.id.signOut).setOnClickListener((viewSignout) -> {
            profileListListener.goToLoginAfterLogout();
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference stoRef = storage.getReference();
            String fileName = UUID.randomUUID().toString() + ".jpg";

            stoRef.child(mauth.getUid()).child(fileName)
                    .putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                createToastMessage("Image uploaded successfully to storage.");
                            }else{
                                createToastMessage(task.getException().getMessage());
                            }
                        }
                    });

            Map<String, Object> addImage = new HashMap<>();
            addImage.put("photoRef", fileName);
            addImage.put("title", imageUri.toString());

            Map<String, Object> profileUser = new HashMap<>();
            profileUser.put("profileUser", mauth.getUid());

            db.collection("profiles")
                    .document(mauth.getUid())
                    .collection("photos")
                    .add(addImage)
                    .addOnCompleteListener((task) -> {
                        if(task.isSuccessful()){
                            createToastMessage("Image added to profile");
                        }else{
                            createToastMessage(task.getException().getMessage());
                        }
                    });
        }
    }

    private void getAllProfiles() {
        profiles.clear();
        db.collection("profiles")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot doc : value.getDocuments()){
                            Map<String, Object> map = doc.getData();
                            Profile profile = new Profile(doc.getId(),
                                    (String)doc.get("name"),
                                    (String)doc.get("email"),
                                    (String)doc.get("password"),
                                    null);
                            profiles.add(profile);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void getPhotoRef(Profile profile) {

    }

    private void getComments(ArrayList<Photo> photosList, String profileId, Photo photo, Profile profile) {

    }

    public void createToastMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ProfileListListener){
            profileListListener = (ProfileListListener) context;
        }else{
            throw new RuntimeException(getActivity()+" must implement ProfileListListener.");
        }
    }

    interface ProfileListListener{
        void goToProfileFragment(Profile profile);
        void goToLoginAfterLogout();
    }
}