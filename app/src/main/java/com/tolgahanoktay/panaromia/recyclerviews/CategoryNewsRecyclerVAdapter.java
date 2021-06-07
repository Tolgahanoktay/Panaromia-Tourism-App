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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.squareup.picasso.Picasso;
import com.tolgahanoktay.panaromia.DetailActivity;
import com.tolgahanoktay.panaromia.R;
import com.tolgahanoktay.panaromia.fragments.FragmentExplore;

import java.util.ArrayList;
import java.util.Collections;

public class CategoryNewsRecyclerVAdapter extends RecyclerView.Adapter<CategoryNewsRecyclerVAdapter.ViewHolder> {
    Context context;
    LayoutInflater layoutInflater;

    private ArrayList<String> placesImage_list;
    private ArrayList<String> placesTitle_list;
    private ArrayList<String> placesContent_list;
    private ArrayList<String> placesYoutube_list;
    private ArrayList<String> placesClickRate_list;
    private ArrayList<String> placesDocumentID_list;
    private ArrayList<String> placesStarRate_list;
    private ArrayList<String> placesCoordinate_list;

    private InterstitialAd cInterstitialAd;

    public CategoryNewsRecyclerVAdapter(Context context, ArrayList<String> placesImage_list, ArrayList<String> placesTitle_list, ArrayList<String> placesContent_list, ArrayList<String> placesYoutube_list, ArrayList<String> placesClickRate_list, ArrayList<String> placesDocumentID_list, ArrayList<String> placesStarRate_list, ArrayList<String> placesCoordinate_list) {
        this.context = context;
        this.placesImage_list = placesImage_list;
        this.placesTitle_list = placesTitle_list;
        this.placesContent_list = placesContent_list;
        this.placesYoutube_list = placesYoutube_list;
        this.placesClickRate_list = placesClickRate_list;
        this.placesDocumentID_list = placesDocumentID_list;
        this.placesStarRate_list = placesStarRate_list;
        this.placesCoordinate_list = placesCoordinate_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.from(context).inflate(R.layout.item_category_news,parent,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.titleCategory.setText(placesTitle_list.get(position));
        holder.contentCategory.setText(placesContent_list.get(position));
        Picasso.get().load(placesImage_list.get(position)).into(holder.categoryImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cInterstitialAd.isLoaded()) {
                    Intent intent = new Intent(context,  DetailActivity.class);
                    intent.putExtra("category_kenImageIntent", placesImage_list.get(position));
                    intent.putExtra("category_titleIntent", placesTitle_list.get(position));
                    intent.putExtra("category_locationIntent", placesTitle_list.get(position));
                    intent.putExtra("category_contentIntent", placesContent_list.get(position));
                    intent.putExtra("category_youtubeIntent", placesYoutube_list.get(position));
                    intent.putExtra("category_clickRate", placesClickRate_list.get(position));
                    intent.putExtra("category_documentId", placesDocumentID_list.get(position));
                    intent.putExtra("category_starRate", placesStarRate_list.get(position));
                    intent.putExtra("category_coordinate", placesCoordinate_list.get(position));
                    intent.putExtra("cameHome","4");
                    context.startActivity(intent);
                    cInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return placesImage_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        KenBurnsView categoryImage;
        TextView titleCategory, contentCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            RequestConfiguration requestConfiguration
                    = new RequestConfiguration.Builder()
                    .setTestDeviceIds(Collections.singletonList("2B00CA07EDD2CAC6C420171BCE6AEAA6"))
                    .build();
            MobileAds.setRequestConfiguration(requestConfiguration);

            categoryImage = itemView.findViewById(R.id.category_newsImage);
            titleCategory = itemView.findViewById(R.id.category_newsTitle);
            contentCategory = itemView.findViewById(R.id.category_newsContent);

            cInterstitialAd = new InterstitialAd(context);
            cInterstitialAd.setAdUnitId("ca-app-pub-5868956382682991/9506407126");
            cInterstitialAd.loadAd(new AdRequest.Builder().build());

            cInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    cInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            });
        }
    }
}
