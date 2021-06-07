package com.tolgahanoktay.panaromia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.StringValue;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    EditText titleText,youtubeText,categoryText,starText,coordinateText,idText,clickRateText,explainText;
    ImageView imagePlaces;
    Button button,button_update,button_delete;
    Bitmap selectedImage;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    Uri imageData;

    String checkIntent;
    String admin_image,admin_placesName,admin_explain,admin_category,admin_youtube,admin_clickRate,admin_adminStarRate,admin_documentName,admin_coordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        idText = findViewById(R.id.contenID);
        imagePlaces = findViewById(R.id.imagePlaces);
        titleText = findViewById(R.id.contenTitle);
        explainText = findViewById(R.id.contentExplain);
        youtubeText = findViewById(R.id.contentYoutube);
        categoryText = findViewById(R.id.contentCategory);
        starText = findViewById(R.id.contentStarRate);
        coordinateText = findViewById(R.id.contentCoordinate);
        clickRateText = findViewById(R.id.contentClickRate);

        button = findViewById(R.id.buttonSend);
        button_update = findViewById(R.id.buttonUpdate);
        button_delete = findViewById(R.id.buttonDelete);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        checkIntent = getIntent().getStringExtra("cameAdmin");
        String getValue = checkIntent;
        int intentValue = Integer.valueOf(getValue);

        admin_documentName = getIntent().getStringExtra("adminDocumentId");

        if (intentValue == 0){
            admin_image = getIntent().getStringExtra("adminImage");
            admin_placesName = getIntent().getStringExtra("adminPlacesName");
            admin_explain = getIntent().getStringExtra("adminContent");
            admin_category = getIntent().getStringExtra("adminCategory");
            admin_youtube = getIntent().getStringExtra("adminyYoutube");
            admin_clickRate = getIntent().getStringExtra("adminClickRate");
            admin_documentName = getIntent().getStringExtra("adminDocumentId");
            admin_adminStarRate = getIntent().getStringExtra("adminStarRate");
            admin_coordinate = getIntent().getStringExtra("AdminCoordinate");

            idText.setVisibility(View.VISIBLE);
            idText.setText(admin_documentName);
            Picasso.get().load(admin_image).into(imagePlaces);
            titleText.setText(admin_placesName);
            explainText.setText(admin_explain);
            categoryText.setText(admin_category);
            youtubeText.setText(admin_youtube);
            starText.setText(admin_adminStarRate);
            coordinateText.setText(admin_coordinate);
            clickRateText.setVisibility(View.VISIBLE);
            clickRateText.setText(admin_clickRate);
            button.setVisibility(View.GONE);
            button_update.setVisibility(View.VISIBLE);
            button_delete.setVisibility(View.VISIBLE);

        }

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(v);
            }
        });

    }

    public void upload(View view){

        if (imageData != null){
            UUID uuid = UUID.randomUUID();
            final  String imageName = "images/" + uuid + ".png";

            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();

                            String titleName = titleText.getText().toString();
                            String explain = explainText.getText().toString();
                            String youtube = youtubeText.getText().toString();
                            String category = categoryText.getText().toString();
                            String coordinate = coordinateText.getText().toString();
                            String starRate = starText.getText().toString();

                            int clickCounter = 0;
                            String counter = String.valueOf(clickCounter);

                            HashMap <String,Object> postData = new HashMap<>();
                            postData.put("placesImage",downloadUrl);
                            postData.put("title", titleName);
                            postData.put("explain" ,explain);
                            postData.put("youtubeCode", youtube);
                            postData.put("category",category);
                            postData.put("coordinate",coordinate);
                            postData.put("starRate",starRate);
                            postData.put("date", FieldValue.serverTimestamp());
                            postData.put("clickCounter", counter);


                            firebaseFirestore.collection("Contents").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });

        }

    }


    public void update(View view){

        String titleName = titleText.getText().toString();
        String explain = explainText.getText().toString();
        String youtube = youtubeText.getText().toString();
        String category = categoryText.getText().toString();
        String coordinate = coordinateText.getText().toString();
        String starRate = starText.getText().toString();
        String clickRate = clickRateText.getText().toString();

        DocumentReference documentReference = firebaseFirestore.collection("Contents").document(admin_documentName);

        documentReference.update("title", titleName,
                "explain",explain,
                "youtubeCode",youtube,
                "category",category,
                "coordinate",coordinate,
                "starRate",starRate,
                "clickCounter",clickRate).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void deleteItem(View view){

        firebaseFirestore.collection("Contents").document(String.valueOf(idText.getText()))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
    }

    public void selectImage(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){

            imageData = data.getData();

            try {
            if (Build.VERSION.SDK_INT >= 28) {
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    imagePlaces.setImageBitmap(selectedImage);
                } else{
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                imagePlaces.setImageBitmap(selectedImage);
            }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}