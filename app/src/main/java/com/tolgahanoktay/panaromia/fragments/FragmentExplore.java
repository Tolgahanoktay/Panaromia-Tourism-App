package com.tolgahanoktay.panaromia.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tolgahanoktay.panaromia.R;
import com.tolgahanoktay.panaromia.recyclerviews.CategoryNewsRecyclerVAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class FragmentExplore extends Fragment {

    RecyclerView recyclerView;
    CategoryNewsRecyclerVAdapter CNews_RecyclerVAdapter;

    FirebaseFirestore firebaseFirestore;

    ArrayList<String> placesImageFBase;
    ArrayList<String> placesTitleFBase;
    ArrayList<String> placesExplainFBase;
    ArrayList<String> placesYoutubeFBase;
    ArrayList<String> placesClickRateFBase;
    ArrayList<String> placesDocumentIDFBase;
    ArrayList<String> placesStarRateFBase;
    ArrayList<String> placesCoordinateFBase;

    TextView textView;

    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore,container,false);

        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(Collections.singletonList("2B00CA07EDD2CAC6C420171BCE6AEAA6"))
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);

        textView = view.findViewById(R.id.choose_categoryText);

        placesImageFBase = new ArrayList<>();
        placesTitleFBase = new ArrayList<>();
        placesExplainFBase = new ArrayList<>();
        placesYoutubeFBase = new ArrayList<>();
        placesClickRateFBase = new ArrayList<>();
        placesDocumentIDFBase = new ArrayList<>();
        placesStarRateFBase = new ArrayList<>();
        placesCoordinateFBase = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView= view.findViewById(R.id.recyclerV_categoryNews);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));


        /*Fragment fragment = new FragmentHome();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout_fragment,fragment);
        transaction.addToBackStack(null);
        transaction.commit();*/

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        String getStoredInfo = sharedPreferences.getString("category","empty");

        if (getStoredInfo.equals("empty")){
            textView.setText("You haven't selected a category yet!");
        }
        else if (getStoredInfo.equals("Art and Culture")){
            loadNewsArtCulture();
            textView.setVisibility(View.GONE);
        }
        else if (getStoredInfo.equals("Summer Holiday")){
            loadNewsSummer();
            textView.setVisibility(View.GONE);
        }
        else if (getStoredInfo.equals("Cruise Holiday")){
            loadNewsCruise();
            textView.setVisibility(View.GONE);
        }
        else if (getStoredInfo.equals("Skiing Holiday")){
            loadNewsSkiing();
            textView.setVisibility(View.GONE);
        }
        else if (getStoredInfo.equals("Hiking Holiday")){
            loadNewsHiking();
            textView.setVisibility(View.GONE);
        }

        Bundle bundle = getArguments();
        if (bundle != null){
            String getCategory = bundle.getString("categoryName");
            if (getCategory.equals("Art and Culture")){
                loadNewsArtCulture();
                textView.setVisibility(View.GONE);
                sharedPreferences.edit().putString("category",getCategory).apply();
            }
            else if (getCategory.equals("Summer Holiday")){
                loadNewsSummer();
                textView.setVisibility(View.GONE);
                sharedPreferences.edit().putString("category",getCategory).apply();
            }
            else if (getCategory.equals("Cruise Holiday")){
                loadNewsCruise();
                textView.setVisibility(View.GONE);
                sharedPreferences.edit().putString("category",getCategory).apply();
            }
            else if (getCategory.equals("Skiing Holiday")){
                loadNewsSkiing();
                textView.setVisibility(View.GONE);
                sharedPreferences.edit().putString("category",getCategory).apply();
            }
            else if (getCategory.equals("Hiking Holiday")){
                loadNewsHiking();
                textView.setVisibility(View.GONE);
                sharedPreferences.edit().putString("category",getCategory).apply();
            }
            else {
                System.out.println("Not Found Bundle");
            }
        }

        return view;
    }

    public void loadNewsArtCulture(){
        CollectionReference collectionReference = firebaseFirestore.collection("Contents");

        collectionReference.whereEqualTo("category", "Art and Culture").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    placesImageFBase.clear();
                    placesTitleFBase.clear();
                    placesExplainFBase.clear();
                    placesYoutubeFBase.clear();
                    placesClickRateFBase.clear();
                    placesDocumentIDFBase.clear();
                    placesStarRateFBase.clear();
                    placesCoordinateFBase.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String placesImage = (String) data.get("placesImage");
                        String placesName = (String) data.get("title");
                        String placesExplain = (String) data.get("explain");
                        String placesYoutube = (String) data.get("youtubeCode");
                        String placesClickRate = (String) data.get("clickCounter");
                        String placesViewRate = (String) data.get("starRate");
                        String placesCoordinate = (String) data.get("coordinate");


                        placesImageFBase.add(placesImage);
                        placesTitleFBase.add(placesName);
                        placesExplainFBase.add(placesExplain);
                        placesYoutubeFBase.add(placesYoutube);
                        placesClickRateFBase.add(placesClickRate);
                        placesDocumentIDFBase.add(documentName);
                        placesStarRateFBase.add(placesViewRate);
                        placesCoordinateFBase.add(placesCoordinate);


                        CNews_RecyclerVAdapter = new CategoryNewsRecyclerVAdapter(getActivity(),placesImageFBase,placesTitleFBase,placesExplainFBase,placesYoutubeFBase,placesClickRateFBase,placesDocumentIDFBase,placesStarRateFBase,placesCoordinateFBase);
                        recyclerView.setAdapter(CNews_RecyclerVAdapter);
                        CNews_RecyclerVAdapter.notifyDataSetChanged();

                    }
                }
            }
        });

    }


    public void loadNewsSummer(){
        CollectionReference collectionReference = firebaseFirestore.collection("Contents");

        collectionReference.whereEqualTo("category", "Summer Holiday").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    placesImageFBase.clear();
                    placesTitleFBase.clear();
                    placesExplainFBase.clear();
                    placesYoutubeFBase.clear();
                    placesClickRateFBase.clear();
                    placesDocumentIDFBase.clear();
                    placesStarRateFBase.clear();
                    placesCoordinateFBase.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String placesImage = (String) data.get("placesImage");
                        String placesName = (String) data.get("title");
                        String placesExplain = (String) data.get("explain");
                        String placesYoutube = (String) data.get("youtubeCode");
                        String placesClickRate = (String) data.get("clickCounter");
                        String placesViewRate = (String) data.get("starRate");
                        String placesCoordinate = (String) data.get("coordinate");


                        placesImageFBase.add(placesImage);
                        placesTitleFBase.add(placesName);
                        placesExplainFBase.add(placesExplain);
                        placesYoutubeFBase.add(placesYoutube);
                        placesClickRateFBase.add(placesClickRate);
                        placesDocumentIDFBase.add(documentName);
                        placesStarRateFBase.add(placesViewRate);
                        placesCoordinateFBase.add(placesCoordinate);


                        CNews_RecyclerVAdapter = new CategoryNewsRecyclerVAdapter(getActivity(),placesImageFBase,placesTitleFBase,placesExplainFBase,placesYoutubeFBase,placesClickRateFBase,placesDocumentIDFBase,placesStarRateFBase,placesCoordinateFBase);
                        recyclerView.setAdapter(CNews_RecyclerVAdapter);
                        CNews_RecyclerVAdapter.notifyDataSetChanged();

                    }
                }
            }
        });

    }

    public void loadNewsCruise(){
        CollectionReference collectionReference = firebaseFirestore.collection("Contents");

        collectionReference.whereEqualTo("category", "Cruise Holiday").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    placesImageFBase.clear();
                    placesTitleFBase.clear();
                    placesExplainFBase.clear();
                    placesYoutubeFBase.clear();
                    placesClickRateFBase.clear();
                    placesDocumentIDFBase.clear();
                    placesStarRateFBase.clear();
                    placesCoordinateFBase.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String placesImage = (String) data.get("placesImage");
                        String placesName = (String) data.get("title");
                        String placesExplain = (String) data.get("explain");
                        String placesYoutube = (String) data.get("youtubeCode");
                        String placesClickRate = (String) data.get("clickCounter");
                        String placesViewRate = (String) data.get("starRate");
                        String placesCoordinate = (String) data.get("coordinate");


                        placesImageFBase.add(placesImage);
                        placesTitleFBase.add(placesName);
                        placesExplainFBase.add(placesExplain);
                        placesYoutubeFBase.add(placesYoutube);
                        placesClickRateFBase.add(placesClickRate);
                        placesDocumentIDFBase.add(documentName);
                        placesStarRateFBase.add(placesViewRate);
                        placesCoordinateFBase.add(placesCoordinate);


                        CNews_RecyclerVAdapter = new CategoryNewsRecyclerVAdapter(getActivity(),placesImageFBase,placesTitleFBase,placesExplainFBase,placesYoutubeFBase,placesClickRateFBase,placesDocumentIDFBase,placesStarRateFBase,placesCoordinateFBase);
                        recyclerView.setAdapter(CNews_RecyclerVAdapter);
                        CNews_RecyclerVAdapter.notifyDataSetChanged();

                    }
                }
            }
        });

    }

    public void loadNewsSkiing(){
        CollectionReference collectionReference = firebaseFirestore.collection("Contents");

        collectionReference.whereEqualTo("category", "Skiing Holiday").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    placesImageFBase.clear();
                    placesTitleFBase.clear();
                    placesExplainFBase.clear();
                    placesYoutubeFBase.clear();
                    placesClickRateFBase.clear();
                    placesDocumentIDFBase.clear();
                    placesStarRateFBase.clear();
                    placesCoordinateFBase.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String placesImage = (String) data.get("placesImage");
                        String placesName = (String) data.get("title");
                        String placesExplain = (String) data.get("explain");
                        String placesYoutube = (String) data.get("youtubeCode");
                        String placesClickRate = (String) data.get("clickCounter");
                        String placesViewRate = (String) data.get("starRate");
                        String placesCoordinate = (String) data.get("coordinate");


                        placesImageFBase.add(placesImage);
                        placesTitleFBase.add(placesName);
                        placesExplainFBase.add(placesExplain);
                        placesYoutubeFBase.add(placesYoutube);
                        placesClickRateFBase.add(placesClickRate);
                        placesDocumentIDFBase.add(documentName);
                        placesStarRateFBase.add(placesViewRate);
                        placesCoordinateFBase.add(placesCoordinate);


                        CNews_RecyclerVAdapter = new CategoryNewsRecyclerVAdapter(getActivity(),placesImageFBase,placesTitleFBase,placesExplainFBase,placesYoutubeFBase,placesClickRateFBase,placesDocumentIDFBase,placesStarRateFBase,placesCoordinateFBase);
                        recyclerView.setAdapter(CNews_RecyclerVAdapter);
                        CNews_RecyclerVAdapter.notifyDataSetChanged();

                    }
                }
            }
        });

    }

    public void loadNewsHiking(){
        CollectionReference collectionReference = firebaseFirestore.collection("Contents");

        collectionReference.whereEqualTo("category", "Hiking Holiday").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    placesImageFBase.clear();
                    placesTitleFBase.clear();
                    placesExplainFBase.clear();
                    placesYoutubeFBase.clear();
                    placesClickRateFBase.clear();
                    placesDocumentIDFBase.clear();
                    placesStarRateFBase.clear();
                    placesCoordinateFBase.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String placesImage = (String) data.get("placesImage");
                        String placesName = (String) data.get("title");
                        String placesExplain = (String) data.get("explain");
                        String placesYoutube = (String) data.get("youtubeCode");
                        String placesClickRate = (String) data.get("clickCounter");
                        String placesViewRate = (String) data.get("starRate");
                        String placesCoordinate = (String) data.get("coordinate");


                        placesImageFBase.add(placesImage);
                        placesTitleFBase.add(placesName);
                        placesExplainFBase.add(placesExplain);
                        placesYoutubeFBase.add(placesYoutube);
                        placesClickRateFBase.add(placesClickRate);
                        placesDocumentIDFBase.add(documentName);
                        placesStarRateFBase.add(placesViewRate);
                        placesCoordinateFBase.add(placesCoordinate);


                        CNews_RecyclerVAdapter = new CategoryNewsRecyclerVAdapter(getActivity(),placesImageFBase,placesTitleFBase,placesExplainFBase,placesYoutubeFBase,placesClickRateFBase,placesDocumentIDFBase,placesStarRateFBase,placesCoordinateFBase);
                        recyclerView.setAdapter(CNews_RecyclerVAdapter);
                        CNews_RecyclerVAdapter.notifyDataSetChanged();

                    }
                }
            }
        });

    }

}
