package com.tolgahanoktay.panaromia.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tolgahanoktay.panaromia.recyclerviews.FavoriesRecyclerVAdapter;
import com.tolgahanoktay.panaromia.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


public class FragmentFavList extends Fragment {

    private FirebaseFirestore firebaseFirestore;

    ArrayList<String> placesImageFBase;
    ArrayList<String> placesTitleFBase;
    ArrayList<String> placesExplainFBase;
    ArrayList<String> placesYoutubeFBase;
    ArrayList<String> placesClickRateFBase;
    ArrayList<String> placesDocumentIDFBase;
    ArrayList<String> placesStarRateFBase;
    ArrayList<String> placesCoordinateFBase;


    RecyclerView recyclerView;
    FavoriesRecyclerVAdapter favoriesRecyclerVAdapter;
    ArrayList<String> documentArray;
    ArrayList<Integer> idArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favories,container,false);

        firebaseFirestore = FirebaseFirestore.getInstance();

        placesImageFBase = new ArrayList<>();
        placesTitleFBase = new ArrayList<>();
        placesExplainFBase = new ArrayList<>();
        placesYoutubeFBase = new ArrayList<>();
        placesClickRateFBase = new ArrayList<>();
        placesDocumentIDFBase = new ArrayList<>();
        placesStarRateFBase = new ArrayList<>();
        placesCoordinateFBase = new ArrayList<>();

        documentArray = new ArrayList<String>();
        idArray = new ArrayList<Integer>();

        recyclerView = view.findViewById(R.id.recycler_view_listFavories);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getData();

        Iterator<String> iterator = documentArray.iterator();

        while (iterator.hasNext()){
            loadNews(iterator.next());
        }

        /*DetailActivity detailActivity = new DetailActivity();
        detailActivity.getArraylist(documentArray);*/

        return view;

    }

    public void getData() {

        try {
            SQLiteDatabase database = getActivity().openOrCreateDatabase("MyFavPlaceList", MODE_PRIVATE, null);

            Cursor cursor = database.rawQuery("SELECT * FROM myfavplacelist", null);
            int documentIdX = cursor.getColumnIndex("documentName");
            int idIx = cursor.getColumnIndex("id");

            while (cursor.moveToNext()) {
                documentArray.add(cursor.getString(documentIdX));
                idArray.add(cursor.getInt(idIx));

            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadNews(String documentID){

        DocumentReference documentReference = firebaseFirestore.collection("Contents").document(documentID);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {


                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Map <String, Object> data =document.getData();

                        String documentName = document.getId();
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


                        favoriesRecyclerVAdapter = new FavoriesRecyclerVAdapter(getActivity(), placesImageFBase, placesTitleFBase, placesExplainFBase, placesYoutubeFBase, placesClickRateFBase, placesDocumentIDFBase,placesStarRateFBase,placesCoordinateFBase);
                        recyclerView.setAdapter(favoriesRecyclerVAdapter);
                        favoriesRecyclerVAdapter.notifyDataSetChanged();


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });
    }

}
