package com.tolgahanoktay.panaromia.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import com.tolgahanoktay.panaromia.recyclerviews.CategoryRecyclerVAdapter;
import com.tolgahanoktay.panaromia.CategoryModel;
import com.tolgahanoktay.panaromia.DetailActivity;
import com.tolgahanoktay.panaromia.R;
import com.tolgahanoktay.panaromia.recyclerviews.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FragmentHome extends Fragment {

    KenBurnsView burnsView;
    TextView textView,headerRate,headerView;

    RecyclerView recyclerViewNews;
    RecyclerViewAdapter recyclerViewAdapterNews;

    RecyclerView recyclerViewCategory;
    CategoryRecyclerVAdapter c_adapter;
    List<CategoryModel> c_models;

    private FirebaseFirestore firebaseFirestore;

    ArrayList<String> placesImageFBase;
    ArrayList<String> placesTitleFBase;
    ArrayList<String> placesExplainFBase;
    ArrayList<String> placesYoutubeFBase;
    ArrayList<String> placesClickRateFBase;
    ArrayList<String> placesDocumentIDFBase;
    ArrayList<String> placesStarRateFBase;
    ArrayList<String> placesCoordinateFBase;

    String documentNameHeader,placesImageHeader,placesNameHeader,placesExplainHeader,placesYoutubeHeader,placesClickRateHeader,places_StarRateHeader,places_Coordinate;

    private InterstitialAd mInterstitialAd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_home,container,false);
        View view =inflater.inflate(R.layout.fragment_home,container,false);


        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(Collections.singletonList("2B00CA07EDD2CAC6C420171BCE6AEAA6"))
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);

        MobileAds.initialize(getActivity(), "ca-app-pub-5868956382682991~8137307520");


        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-5868956382682991/9506407126");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });



        recyclerViewNews = view.findViewById(R.id.recyclerNews);
        recyclerViewNews.setHasFixedSize(true);
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        recyclerViewNews.setPadding(5, 0, 10, 0);

        burnsView = view.findViewById(R.id.kenImage_home);
        textView = view.findViewById(R.id.home_text_placeName);
        headerRate = view.findViewById(R.id.headerStarRate);
        headerView = view.findViewById(R.id.headerView);

        firebaseFirestore = FirebaseFirestore.getInstance();

        placesImageFBase = new ArrayList<>();
        placesTitleFBase = new ArrayList<>();
        placesExplainFBase = new ArrayList<>();
        placesYoutubeFBase = new ArrayList<>();
        placesClickRateFBase = new ArrayList<>();
        placesDocumentIDFBase = new ArrayList<>();
        placesStarRateFBase = new ArrayList<>();
        placesCoordinateFBase = new ArrayList<>();


        recyclerViewCategory = view.findViewById(R.id.recyclerCategory);
        recyclerViewCategory.setHasFixedSize(true);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        loadNewsAll();
        loadHeader();

        burnsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("kenImageHeader", placesImageHeader);
                intent.putExtra("titleIntentHeader", placesNameHeader);
                intent.putExtra("locationIntentHeader", placesNameHeader);
                intent.putExtra("contentIntentHeader", placesExplainHeader);
                intent.putExtra("youtubeIntentHeader", placesYoutubeHeader);
                intent.putExtra("clickRateHeader", placesClickRateHeader);
                intent.putExtra("documentIdHeader",documentNameHeader);
                intent.putExtra("starRateHeader",places_StarRateHeader);
                intent.putExtra("coordinateHeader",places_Coordinate);
                intent.putExtra("cameHome","3");
                getActivity().startActivity(intent);
                mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });

        c_models = new ArrayList<>();
        c_models.add(new CategoryModel(R.drawable.icons8_cologne_cathedral_30px, "Art & Culture"));
        c_models.add(new CategoryModel(R.drawable.icons8_swimming_30px, "Summer Holiday"));
        c_models.add(new CategoryModel(R.drawable.icons8_cruise_ship_30px, "Cruise Holiday"));
        c_models.add(new CategoryModel(R.drawable.icons8_alpine_skiing_30px, "Skiing Holiday"));
        c_models.add(new CategoryModel(R.drawable.icons8_trekking_30px, "Hiking Holiday"));

        c_adapter = new CategoryRecyclerVAdapter(getActivity(),c_models);
        recyclerViewCategory.setAdapter(c_adapter);
        recyclerViewCategory.setPadding(20,0,10,0);

        return view;
    }

    public void loadNewsAll(){
        CollectionReference collectionReference = firebaseFirestore.collection("Contents");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        Map <String, Object> data =snapshot.getData();

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

                        recyclerViewAdapterNews = new RecyclerViewAdapter(getActivity(),placesImageFBase,placesTitleFBase,placesExplainFBase,placesYoutubeFBase,placesClickRateFBase,placesDocumentIDFBase,placesStarRateFBase,placesCoordinateFBase);
                        recyclerViewNews.setAdapter(recyclerViewAdapterNews);
                        recyclerViewAdapterNews.notifyDataSetChanged();


                    }
                }
            }
        });

    }

    public void loadHeader(){
        CollectionReference collectionReference = firebaseFirestore.collection("Contents");

        collectionReference.orderBy("clickCounter", Query.Direction.DESCENDING).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){


                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map <String, Object> data =snapshot.getData();

                         documentNameHeader = snapshot.getId();
                         placesImageHeader = (String) data.get("placesImage");
                         placesNameHeader = (String) data.get("title");
                         placesExplainHeader = (String) data.get("explain");
                         placesYoutubeHeader = (String) data.get("youtubeCode");
                         placesClickRateHeader = (String) data.get("clickCounter");
                         places_StarRateHeader = (String) data.get("starRate");
                         places_Coordinate = (String) data.get("coordinate");

                        Picasso.get().load(placesImageHeader).into(burnsView);
                        textView.setText(placesNameHeader);
                        headerRate.setText(places_StarRateHeader);
                        headerView.setText(placesClickRateHeader);


                    }
                }
            }
        });

    }
}
