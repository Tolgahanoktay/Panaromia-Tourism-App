package com.tolgahanoktay.panaromia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tolgahanoktay.panaromia.recyclerviews.AdminRecyclerVAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class AdminActivity extends AppCompatActivity {
    RecyclerView admin_recyclerVAdapter;
    AdminRecyclerVAdapter adminRecyclerVAdapter;

    private FirebaseFirestore firebaseFirestore;

    ArrayList<String> admin_image;
    ArrayList<String> admin_placesName;
    ArrayList<String> admin_explain;
    ArrayList<String> admin_category;
    ArrayList<String> admin_youtube;
    ArrayList<String> admin_clickRate;
    ArrayList<String> admin_adminStarRate;
    ArrayList<Date> admin_date;
    ArrayList<String> admin_documentName;
    ArrayList<String> admin_coordinate;

    Button button_enterData,btn_go_to_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        admin_recyclerVAdapter = findViewById(R.id.recyclerVAdmin);
        admin_recyclerVAdapter.setHasFixedSize(true);
        admin_recyclerVAdapter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        admin_image = new ArrayList<>();
        admin_placesName = new ArrayList<>();
        admin_category = new ArrayList<>();
        admin_date = new ArrayList<Date>();
        admin_documentName = new ArrayList<>();
        admin_explain = new ArrayList<>();
        admin_youtube = new ArrayList<>();
        admin_clickRate = new ArrayList<>();
        admin_adminStarRate = new ArrayList<>();
        admin_coordinate = new ArrayList<>();

        button_enterData = findViewById(R.id.btn_setData);
        btn_go_to_app = findViewById(R.id.btn_goToApp);

        button_enterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UploadActivity.class);
                intent.putExtra("cameAdmin","1");
                startActivity(intent);
            }
        });

        btn_go_to_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomePage.class);
                startActivity(intent);
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();

        getAdminContent();
    }

    public void getAdminContent(){
        CollectionReference collectionReference = firebaseFirestore.collection("Contents");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null){

                    admin_image.clear();
                    admin_placesName.clear();
                    admin_category.clear();
                    admin_date.clear();
                    admin_documentName.clear();


                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String, Object> data =snapshot.getData();

                        String documentName = snapshot.getId();
                        String placesImage = (String) data.get("placesImage");
                        String placesName = (String) data.get("title");
                        String placesContent = (String) data.get("explain");
                        String placesCategory = (String) data.get("category");
                        String placesYoutube = (String) data.get("youtubeCode");
                        String placesClickRate = (String) data.get("clickCounter");
                        String placesStarRate = (String) data.get("starRate");
                        String placesCoordinate = (String) data.get("coordinate");
                        Date placesDate = snapshot.getTimestamp("date").toDate();;

                        admin_image.add(placesImage);
                        admin_placesName.add(placesName);
                        admin_explain.add(placesContent);
                        admin_category.add(placesCategory);
                        admin_youtube.add(placesYoutube);
                        admin_clickRate.add(placesClickRate);
                        admin_adminStarRate.add(placesStarRate);
                        admin_coordinate.add(placesCoordinate);
                        admin_date.add(placesDate);
                        admin_documentName.add(documentName);


                        adminRecyclerVAdapter = new AdminRecyclerVAdapter(getApplicationContext(),admin_image,admin_placesName,admin_explain,admin_category,admin_youtube,admin_clickRate,admin_adminStarRate,admin_date,admin_documentName,admin_coordinate);
                        admin_recyclerVAdapter.setAdapter(adminRecyclerVAdapter);
                        adminRecyclerVAdapter.notifyDataSetChanged();


                    }
                }
            }
        });
    }

}