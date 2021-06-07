package com.tolgahanoktay.panaromia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.onesignal.OneSignal;
import com.tolgahanoktay.panaromia.recyclerviews.SearchRecyclerVAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StartingPage extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    Animation animationTop,animationBottom;
    ImageView imageView;
    TextView textViewName,textViewSlogan;

    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> documentValue;

    PublisherAdView mpublisherAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_starting_page);

        /*RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(Collections.singletonList("2B00CA07EDD2CAC6C420171BCE6AEAA6"))
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);*/


        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


        firebaseFirestore = FirebaseFirestore.getInstance();
        documentValue = new ArrayList<>();


        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(final String userId, String registrationId) {
                System.out.println("user id: " + userId);

                CollectionReference collectionReference = firebaseFirestore.collection("NotificationPlayers");
                collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                        }

                        if (queryDocumentSnapshots != null) {

                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> data = snapshot.getData();

                                String documentName = snapshot.getId();
                                String push_playerID = (String) data.get("playerID");

                                documentValue.add(push_playerID);

                                for (String s : documentValue) {
                                    System.out.println("Players Id: " + s);
                                }

                                if (!documentValue.contains(userId)){
                                    setOneSignalID(userId);
                                }
                            }
                        }
                    }
                });
            }
        });


        animationTop = AnimationUtils.loadAnimation(this,R.anim.top_animator);
        animationBottom = AnimationUtils.loadAnimation(this,R.anim.bottom_animator);

        imageView = findViewById(R.id.appLogo);
        textViewName = findViewById(R.id.appName);
        textViewSlogan = findViewById(R.id.appSlogan);

        //imageView.animate().translationY(700).setDuration(1800).setStartDelay(4300);

        imageView.setAnimation(animationTop);
        textViewName.setAnimation(animationBottom);
        textViewSlogan.setAnimation(animationBottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),HomePage.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);

        //MobileAds.initialize(this, "ca-app-pub-5868956382682991~8137307520");
        /*MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });*/
    }


    public void setOneSignalID(String getID){

        Map<String, Object> pushUsers = new HashMap<>();
        pushUsers.put("playerID", getID);

        firebaseFirestore.collection("NotificationPlayers").add(pushUsers).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StartingPage.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}