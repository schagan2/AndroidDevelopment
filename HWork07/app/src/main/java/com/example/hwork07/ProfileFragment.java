package com.example.hwork07;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Profile profile;
    private Profile currentProfile;

    ProfileFragmentListener listener;
    TextView profileName;
    RecyclerView photosList;
    PhotoListAdapter adapter;
    LinearLayoutManager layoutManager;
    FirebaseFirestore db;
    ArrayList<Photo> photosArrayList = new ArrayList<>();

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param profile Parameter 1.
     * @param currentProfile Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(Profile profile, Profile currentProfile) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, profile);
        args.putSerializable(ARG_PARAM2, currentProfile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            profile = (Profile) getArguments().getSerializable(ARG_PARAM1);
            currentProfile = (Profile) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle(R.string.profile_title);
        db = FirebaseFirestore.getInstance();
        profileName = view.findViewById(R.id.profileUserName);
        photosList = view.findViewById(R.id.profilePhotosList);

        profileName.setText(profile.getName());
        Log.d("TAG", "onCreateView: "+profile.getPhotoRefs());

        photosList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        photosList.setLayoutManager(layoutManager);

        adapter = new PhotoListAdapter(photosArrayList, profile, currentProfile, getContext(), this);
        photosList.setAdapter(adapter);

        getPhotoRefs();
        return view;
    }

    void getPhotoRefs(){
        db.collection("profiles")
                .document(profile.getProfileId())
                .collection("photos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        photosArrayList.clear();
                        for(DocumentSnapshot doc : value.getDocuments()){
                            Map<String, Object> map = doc.getData();
                            List<String> likedBy = (List<String>) map.get("likedBy");
                            Uri photoRef = Uri.parse((String) map.get("photoRef"));
                            String title = (String) map.get("title");
                            //String photoId, List<Comment> commentList, List<Profile> likedBy, Uri photoRef, String title
                            Photo photo = new Photo(doc.getId(),
                                    null,
                                    likedBy,
                                    photoRef,
                                    title);
                            photosArrayList.add(photo);
                        }
                        profile.setPhotoRefs(photosArrayList);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ProfileFragmentListener){
            listener = (ProfileFragmentListener) context;
        }else{
            throw new RuntimeException(getContext() +" must implement ProfileFragmentListener.");
        }
    }

    interface ProfileFragmentListener{

    }
}