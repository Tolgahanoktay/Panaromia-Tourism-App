package com.tolgahanoktay.panaromia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.tolgahanoktay.panaromia.fragments.FragmentExplore;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    KenBurnsView burnsView;
    TextView title,locationText,content,starRate,viewsRate;
    YouTubePlayerView playerView;
    ImageView marker;
    Button buttonFav;
    LinearLayout layout;
    SQLiteDatabase sqLiteDatabase;

    String getTitle,getLocation,getContent,getYoutube,getKenBurns,getDocId,getRate,getStarRate,getCoordinate;

    String searchget_Title,searchget_Location,searchget_Content,searchget_Youtube,searchget_KenBurns,searchget_DocId,searchget_Rate,searchget_StarRate,searchget_Coordinate;

    String favoriesget_Title,favoriesget_Location,favoriesget_Content,favoriesget_Youtube,favoriesget_KenBurns,favoriesget_DocId,favoriesget_Rate,favoriesget_StarRate,favoriesget_Coordinate;

    String headerget_Title,headerget_Location,headerget_Content,headerget_Youtube,headerget_KenBurns,headerget_DocId,headerget_Rate,headerget_StarRate,headerget_Coordinate;

    String category_get_Title,category_get_Location,category_get_Content,category_get_Youtube,category_get_KenBurns,category_get_DocId,category_get_Rate,category_get_StarRate,category_get_Coordinate;

    private int ClicksCounter = 0;
    int counterFav = 2;
    private FirebaseFirestore firebaseFirestore;

    String chechkExtrasHome;
    ArrayList<String> documentArray;
    ArrayList<Integer> idArray;

    String takeHomeDocId,takeSearchDocId,takeFavDocId,takeHeaderDocId,takeCategoryDocId;

    private AdView mAdView,mAdViewBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        mAdViewBottom = findViewById(R.id.adViewBottom);
        AdRequest adRequestBottom = new AdRequest.Builder().build();
        mAdViewBottom.loadAd(adRequestBottom);

        mAdViewBottom.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        firebaseFirestore = FirebaseFirestore.getInstance();

        documentArray = new ArrayList<String>();
        idArray = new ArrayList<Integer>();

        burnsView = findViewById(R.id.detail_kenImage);
        title = findViewById(R.id.detail_title);
        locationText = findViewById(R.id.detail_location);
        marker = findViewById(R.id.detail_marker);
        content = findViewById(R.id.detail_content);
        playerView = findViewById(R.id.detail_youtube);
        buttonFav = findViewById(R.id.button_favorite);
        starRate = findViewById(R.id.detailStarRate);
        viewsRate = findViewById(R.id.detailViews);
        getLifecycle().addObserver(playerView);
        layout = findViewById(R.id.getRouteLinear);

        chechkExtrasHome = getIntent().getStringExtra("cameHome");
        String getCheckValue = chechkExtrasHome;
        final int parseCheck = Integer.valueOf(getCheckValue);

        getDocId = getIntent().getStringExtra("documentId");
        searchget_DocId = getIntent().getStringExtra("SdocumentId");
        favoriesget_DocId = getIntent().getStringExtra("FavdocumentId");
        headerget_DocId = getIntent().getStringExtra("documentIdHeader");
        category_get_DocId = getIntent().getStringExtra("category_documentId");

        takeHomeDocId = getDocId;
        takeSearchDocId = searchget_DocId;
        takeFavDocId = favoriesget_DocId;
        takeHeaderDocId = headerget_DocId;
        takeCategoryDocId = category_get_DocId;


        if (parseCheck == 0){

            getKenBurns = getIntent().getStringExtra("kenImageIntent");
            getTitle = getIntent().getStringExtra("titleIntent");
            getLocation = getIntent().getStringExtra("locationIntent");
            getContent = getIntent().getStringExtra("contentIntent");
            getYoutube = getIntent().getStringExtra("youtubeIntent");
            getRate = getIntent().getStringExtra("clickRate");
            getDocId = getIntent().getStringExtra("documentId");
            getStarRate = getIntent().getStringExtra("starRate");
            getCoordinate = getIntent().getStringExtra("coordinate");

            title.setText(getTitle);
            locationText.setText(getLocation);
            content.setText(getContent);
            starRate.setText(getStarRate);
            viewsRate.setText(getRate);

            Picasso.get().load(getKenBurns).into(burnsView);

            playerView.enterFullScreen();
            playerView.exitFullScreen();

            playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    String videoId = getYoutube;
                    youTubePlayer.cueVideo(videoId,0);
                    super.onReady(youTubePlayer);
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("geo:" + getCoordinate));
                    Intent chooser = Intent.createChooser(intent,"Launch Maps");
                    startActivity(chooser);
                }
            });

            visitsComeHome();

        }else if (parseCheck == 1){

            searchget_KenBurns = getIntent().getStringExtra("SkenImageIntent");
            searchget_Title = getIntent().getStringExtra("StitleIntent");
            searchget_Location = getIntent().getStringExtra("SlocationIntent");
            searchget_Content = getIntent().getStringExtra("ScontentIntent");
            searchget_Youtube = getIntent().getStringExtra("SyoutubeIntent");
            searchget_Rate = getIntent().getStringExtra("SclickRate");
            searchget_DocId = getIntent().getStringExtra("SdocumentId");
            searchget_StarRate = getIntent().getStringExtra("SstarRate");
            searchget_Coordinate = getIntent().getStringExtra("Scoordinate");

            title.setText(searchget_Title);
            locationText.setText(searchget_Location);
            content.setText(searchget_Content);
            starRate.setText(searchget_StarRate);
            viewsRate.setText(searchget_Rate);

            Picasso.get().load(searchget_KenBurns).into(burnsView);

            playerView.enterFullScreen();
            playerView.exitFullScreen();

            playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    String videoId = searchget_Youtube;
                    youTubePlayer.cueVideo(videoId,0);
                    super.onReady(youTubePlayer);
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("geo:" + searchget_Coordinate));
                    Intent chooser = Intent.createChooser(intent,"Launch Maps");
                    startActivity(chooser);
                }
            });


            visitsComeSearch();


        }else if (parseCheck == 2){

            favoriesget_KenBurns = getIntent().getStringExtra("FavkenImageIntent");
            favoriesget_Title = getIntent().getStringExtra("FavtitleIntent");
            favoriesget_Location = getIntent().getStringExtra("FavlocationIntent");
            favoriesget_Content = getIntent().getStringExtra("FavcontentIntent");
            favoriesget_Youtube = getIntent().getStringExtra("FavyoutubeIntent");
            favoriesget_Rate = getIntent().getStringExtra("FavclickRate");
            favoriesget_DocId = getIntent().getStringExtra("FavdocumentId");
            favoriesget_StarRate = getIntent().getStringExtra("FavstarRate");
            favoriesget_Coordinate = getIntent().getStringExtra("Favcoordinate");

            title.setText(favoriesget_Title);
            locationText.setText(favoriesget_Location);
            content.setText(favoriesget_Content);
            starRate.setText(favoriesget_StarRate);
            viewsRate.setText(favoriesget_Rate);

            Picasso.get().load(favoriesget_KenBurns).into(burnsView);

            playerView.enterFullScreen();
            playerView.exitFullScreen();

            playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    String videoId = favoriesget_Youtube;
                    youTubePlayer.cueVideo(videoId,0);
                    super.onReady(youTubePlayer);
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("geo:" +  favoriesget_Coordinate));
                    Intent chooser = Intent.createChooser(intent,"Launch Maps");
                    startActivity(chooser);
                }
            });

            visitsComeFavories();

        }else if (parseCheck == 3){


            headerget_KenBurns = getIntent().getStringExtra("kenImageHeader");
            headerget_Title = getIntent().getStringExtra("titleIntentHeader");
            headerget_Location = getIntent().getStringExtra("locationIntentHeader");
            headerget_Content = getIntent().getStringExtra("contentIntentHeader");
            headerget_Youtube = getIntent().getStringExtra("youtubeIntentHeader");
            headerget_Rate = getIntent().getStringExtra("clickRateHeader");
            headerget_DocId = getIntent().getStringExtra("documentIdHeader");
            headerget_StarRate = getIntent().getStringExtra("starRateHeader");
            headerget_Coordinate = getIntent().getStringExtra("coordinateHeader");

            title.setText(headerget_Title);
            locationText.setText(headerget_Location);
            content.setText(headerget_Content);
            starRate.setText(headerget_StarRate);
            viewsRate.setText(headerget_Rate);

            Picasso.get().load(headerget_KenBurns).into(burnsView);

            playerView.enterFullScreen();
            playerView.exitFullScreen();

            playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    String videoId = headerget_Youtube;
                    youTubePlayer.cueVideo(videoId,0);
                    super.onReady(youTubePlayer);
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("geo:" +  headerget_Coordinate));
                    Intent chooser = Intent.createChooser(intent,"Launch Maps");
                    startActivity(chooser);
                }
            });


            visitsComeHeader();

        }
        else if (parseCheck == 4){

            category_get_KenBurns = getIntent().getStringExtra("category_kenImageIntent");
            category_get_Title = getIntent().getStringExtra("category_titleIntent");
            category_get_Location = getIntent().getStringExtra("category_locationIntent");
            category_get_Content = getIntent().getStringExtra("category_contentIntent");
            category_get_Youtube = getIntent().getStringExtra("category_youtubeIntent");
            category_get_Rate = getIntent().getStringExtra("category_clickRate");
            category_get_DocId = getIntent().getStringExtra("category_documentId");
            category_get_StarRate = getIntent().getStringExtra("category_starRate");
            category_get_Coordinate = getIntent().getStringExtra("category_coordinate");

            title.setText(category_get_Title);
            locationText.setText(category_get_Location);
            content.setText(category_get_Content);
            starRate.setText(category_get_StarRate);
            viewsRate.setText(category_get_Rate);

            Picasso.get().load(category_get_KenBurns).into(burnsView);

            playerView.enterFullScreen();
            playerView.exitFullScreen();

            playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    String videoId = category_get_Youtube;
                    youTubePlayer.cueVideo(videoId,0);
                    super.onReady(youTubePlayer);
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("geo:" +  category_get_Coordinate));
                    Intent chooser = Intent.createChooser(intent,"Launch Maps");
                    startActivity(chooser);
                }
            });

            visitsComeCategory();

        }
        else{
            Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
        }



        buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                counterFav++;

                if (counterFav % 2 == 1){
                    buttonFav.setBackgroundResource(R.drawable.icons8_heart_35px);

                    if (parseCheck == 0){

                        savePlaces(getDocId);


                    }
                    else if (parseCheck == 1){

                        savePlaces(searchget_DocId);

                    }else if (parseCheck == 2){

                        savePlaces(favoriesget_DocId);

                    }
                    else if (parseCheck == 3){

                        savePlaces(headerget_DocId);

                    }
                    else if (parseCheck == 4){

                        savePlaces(category_get_DocId);

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
                    }

                }else{
                    buttonFav.setBackgroundResource(R.drawable.icons8_heart_35px_1);

                    if (parseCheck == 0){

                        deletePlaces(getDocId);

                    }
                    else if (parseCheck == 1){

                        deletePlaces(searchget_DocId);

                    }else if (parseCheck == 2){

                        deletePlaces(favoriesget_DocId);

                    }else if (parseCheck == 3){

                        deletePlaces(headerget_DocId);

                    }
                    else if (parseCheck == 4){

                        deletePlaces(category_get_DocId);

                    }
                    else{

                        Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        getData();

        for (int i=0; i<documentArray.size(); i++) {
            if (documentArray.contains(takeHomeDocId) || documentArray.contains(takeSearchDocId) || documentArray.contains(takeFavDocId) || documentArray.contains(takeHeaderDocId) || documentArray.contains(takeCategoryDocId)) {
                buttonFav.setBackgroundResource(R.drawable.icons8_heart_35px);
                System.out.println("Document name : " + documentArray.get(i));
            } else {
                buttonFav.setBackgroundResource(R.drawable.icons8_heart_35px_1);
            }
        }


    }

    private void visitsComeHome(){
        int rate = Integer.valueOf(getRate);
        int plusRate = rate + 1;
        String sendRate = String.valueOf(plusRate);

        DocumentReference documentReference = firebaseFirestore.collection("Contents").document(getDocId);

        documentReference.update("clickCounter", sendRate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getApplicationContext(),"ClickCounter Successfully Updated",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Please Check Your Network Connection",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void visitsComeSearch(){
        int rate = Integer.valueOf(searchget_Rate);
        int plusRate = rate + 1;
        String sendRate = String.valueOf(plusRate);

        DocumentReference documentReference = firebaseFirestore.collection("Contents").document(searchget_DocId);

        documentReference.update("clickCounter", sendRate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getApplicationContext(),"ClickCounter Successfully Updated",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Please Check Your Network Connection",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void visitsComeFavories(){
        int rate = Integer.valueOf(favoriesget_Rate);
        int plusRate = rate + 1;
        String sendRate = String.valueOf(plusRate);

        DocumentReference documentReference = firebaseFirestore.collection("Contents").document(favoriesget_DocId);

        documentReference.update("clickCounter", sendRate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getApplicationContext(),"ClickCounter Successfully Updated",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Please Check Your Network Connection",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void visitsComeHeader(){
        int rate = Integer.valueOf(headerget_Rate);
        int plusRate = rate + 1;
        String sendRate = String.valueOf(plusRate);

        DocumentReference documentReference = firebaseFirestore.collection("Contents").document(headerget_DocId);

        documentReference.update("clickCounter", sendRate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getApplicationContext(),"ClickCounter Successfully Updated",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Please Check Your Network Connection",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void visitsComeCategory(){
        int rate = Integer.valueOf(category_get_Rate);
        int plusRate = rate + 1;
        String sendRate = String.valueOf(plusRate);

        DocumentReference documentReference = firebaseFirestore.collection("Contents").document(category_get_DocId);

        documentReference.update("clickCounter", sendRate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getApplicationContext(),"ClickCounter Successfully Updated",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Please Check Your Network Connection",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void savePlaces(String documentId){

        sqLiteDatabase = this.openOrCreateDatabase("MyFavPlaceList",MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS myfavplacelist(id INTEGER PRİMARY KEY, documentName VARCHAR)");

        String sendQuery = "INSERT INTO myfavplacelist (documentName) VALUES (?)";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sendQuery);
        sqLiteStatement.bindString(1,documentId);
        sqLiteStatement.execute();
    }

    public void deletePlaces(String documentId){

        sqLiteDatabase = this.openOrCreateDatabase("MyFavPlaceList",MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS myfavplacelist(id INTEGER PRİMARY KEY, documentName VARCHAR)");

        String sqlString = "DELETE FROM myfavplacelist WHERE documentName = ?";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sqlString);
        sqLiteStatement.bindString(1,documentId);
        sqLiteStatement.execute();
    }

   public void getData() {

       try {
           SQLiteDatabase database = this.openOrCreateDatabase("MyFavPlaceList", MODE_PRIVATE, null);

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
   
}
