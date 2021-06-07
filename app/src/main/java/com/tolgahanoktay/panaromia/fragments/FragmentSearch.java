package com.tolgahanoktay.panaromia.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tolgahanoktay.panaromia.R;
import com.tolgahanoktay.panaromia.recyclerviews.SearchRecyclerVAdapter;

import java.util.ArrayList;
import java.util.Map;

public class FragmentSearch extends Fragment {

    RecyclerView recyclerView;
    SearchRecyclerVAdapter searchRecyclerVAdapter;

    FirebaseFirestore firebaseFirestore;

    ArrayList<String> placesImageFBase;
    ArrayList<String> placesTitleFBase;
    ArrayList<String> placesExplainFBase;
    ArrayList<String> placesYoutubeFBase;
    ArrayList<String> placesClickRateFBase;
    ArrayList<String> placesDocumentIDFBase;
    ArrayList<String> placesStarRateFBase;
    ArrayList<String> placesCoordinateFBase;

    EditText editText;
    //SearchView searchView;
    public int id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);

        placesImageFBase = new ArrayList<>();
        placesTitleFBase = new ArrayList<>();
        placesExplainFBase = new ArrayList<>();
        placesYoutubeFBase = new ArrayList<>();
        placesClickRateFBase = new ArrayList<>();
        placesDocumentIDFBase = new ArrayList<>();
        placesStarRateFBase = new ArrayList<>();
        placesCoordinateFBase = new ArrayList<>();

        //searchView = view.findViewById(R.id.search_dataText);
        editText = view.findViewById(R.id.search_dataText);

        recyclerView= view.findViewById(R.id.recycler_view_searchBox);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseFirestore = FirebaseFirestore.getInstance();


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //loadNewsBefore();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               String getQuery = String.valueOf(editText.getText());
                //String getToText = s.toString();
                if (!getQuery.trim().isEmpty()){
                    loadNewsQuery(getQuery);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        return view;
    }


    public void loadNewsQuery(String infoText) {

            CollectionReference collectionReference = firebaseFirestore.collection("Contents");

            /*collectionReference.orderBy("title").startAt(infoText.toUpperCase()).endAt(infoText.toUpperCase() + "\uf8ff")
            collectionReference.whereEqualTo("title",infoText).startAt(infoText.toUpperCase()).endAt(infoText.toLowerCase() + "\uf8ff")
            collectionReference.whereGreaterThan("title",infoText).orderBy("title").startAt(infoText.toUpperCase()).endAt(infoText.toUpperCase() + "\uf8ff")*/

            collectionReference.whereGreaterThanOrEqualTo("title",infoText).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    if (e != null) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                    }

                    if (queryDocumentSnapshots != null) {

                        placesImageFBase.clear();
                        placesTitleFBase.clear();
                        placesExplainFBase.clear();
                        placesYoutubeFBase.clear();
                        placesClickRateFBase.clear();
                        placesDocumentIDFBase.clear();
                        placesStarRateFBase.clear();
                        placesCoordinateFBase.clear();

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            Map<String, Object> data = snapshot.getData();

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


                            searchRecyclerVAdapter = new SearchRecyclerVAdapter(getActivity(), placesImageFBase, placesTitleFBase, placesExplainFBase, placesYoutubeFBase, placesClickRateFBase, placesDocumentIDFBase,placesStarRateFBase,placesCoordinateFBase);
                            recyclerView.setAdapter(searchRecyclerVAdapter);
                            searchRecyclerVAdapter.notifyDataSetChanged();

                        }
                    }
                }
            });
        }
}
