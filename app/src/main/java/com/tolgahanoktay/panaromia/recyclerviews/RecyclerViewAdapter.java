package com.tolgahanoktay.panaromia.recyclerviews;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.squareup.picasso.Picasso;
import com.tolgahanoktay.panaromia.DetailActivity;
import com.tolgahanoktay.panaromia.R;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    private ArrayList<String> placesImage_list;
    private ArrayList<String> placesName_list;
    private ArrayList<String> placesExplain_list;
    private ArrayList<String> placesYoutube_list;
    private ArrayList<String> placesClickRate_list;
    private ArrayList<String> placesDocumentID_list;
    private ArrayList<String> placesStarRate_list;
    private ArrayList<String> placesCoordinate_list;

    private InterstitialAd mInterstitialAd;

    public RecyclerViewAdapter(Context context, ArrayList<String> placesImage_list, ArrayList<String> placesName_list, ArrayList<String> placesExplain_list, ArrayList<String> placesYoutube_list, ArrayList<String> placesClickRate_list, ArrayList<String> placesDocumentID_list, ArrayList<String> placesStarRate_list, ArrayList<String> placesCoordinate_list) {
        this.context = context;
        this.placesImage_list = placesImage_list;
        this.placesName_list = placesName_list;
        this.placesExplain_list = placesExplain_list;
        this.placesYoutube_list = placesYoutube_list;
        this.placesClickRate_list = placesClickRate_list;
        this.placesDocumentID_list = placesDocumentID_list;
        this.placesStarRate_list = placesStarRate_list;
        this.placesCoordinate_list = placesCoordinate_list;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.title.setText(placesName_list.get(position));
        holder.desc.setText(placesExplain_list.get(position));
        Picasso.get().load(placesImage_list.get(position)).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    Intent intent = new Intent(context,  DetailActivity.class);
                    intent.putExtra("kenImageIntent", placesImage_list.get(position));
                    intent.putExtra("titleIntent", placesName_list.get(position));
                    intent.putExtra("locationIntent", placesName_list.get(position));
                    intent.putExtra("contentIntent", placesExplain_list.get(position));
                    intent.putExtra("youtubeIntent", placesYoutube_list.get(position));
                    intent.putExtra("clickRate", placesClickRate_list.get(position));
                    intent.putExtra("documentId", placesDocumentID_list.get(position));
                    intent.putExtra("starRate", placesStarRate_list.get(position));
                    intent.putExtra("coordinate", placesCoordinate_list.get(position));
                    intent.putExtra("cameHome","0");
                    mInterstitialAd.show();
                    context.startActivity(intent);

                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                    Intent intent = new Intent(context,  DetailActivity.class);
                    intent.putExtra("kenImageIntent", placesImage_list.get(position));
                    intent.putExtra("titleIntent", placesName_list.get(position));
                    intent.putExtra("locationIntent", placesName_list.get(position));
                    intent.putExtra("contentIntent", placesExplain_list.get(position));
                    intent.putExtra("youtubeIntent", placesYoutube_list.get(position));
                    intent.putExtra("clickRate", placesClickRate_list.get(position));
                    intent.putExtra("documentId", placesDocumentID_list.get(position));
                    intent.putExtra("starRate", placesStarRate_list.get(position));
                    intent.putExtra("coordinate", placesCoordinate_list.get(position));
                    intent.putExtra("cameHome","0");
                    context.startActivity(intent);

                }

            }
        });
    }

    @Override
    public int getItemCount() {

        return placesName_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,desc;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            MobileAds.initialize(context, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });

            RequestConfiguration requestConfiguration
                    = new RequestConfiguration.Builder()
                    .setTestDeviceIds(Collections.singletonList("2B00CA07EDD2CAC6C420171BCE6AEAA6"))
                    .build();
            MobileAds.setRequestConfiguration(requestConfiguration);


            title = itemView.findViewById(R.id.places_title);
            desc = itemView.findViewById(R.id.places_description);
            imageView = itemView.findViewById(R.id.image);


            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId("ca-app-pub-5868956382682991/9506407126");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            });

        }

    }


}
