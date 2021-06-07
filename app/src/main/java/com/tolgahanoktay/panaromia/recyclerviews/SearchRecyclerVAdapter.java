package com.tolgahanoktay.panaromia.recyclerviews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tolgahanoktay.panaromia.DetailActivity;
import com.tolgahanoktay.panaromia.R;

import java.util.ArrayList;

public class SearchRecyclerVAdapter extends RecyclerView.Adapter<SearchRecyclerVAdapter.ViewHolder>{

    Context context;
    LayoutInflater layoutInflater;

    private ArrayList<String> getSearchImageDB;
    private ArrayList<String> getSTitleDB;
    private ArrayList<String> getSContentDB;
    private ArrayList<String> getSYoutube_list;
    private ArrayList<String> getSClickRate_list;
    private ArrayList<String> getSDocumentID_list;
    private ArrayList<String> placesStarRate_list;
    private ArrayList<String> placesCoordinate_list;

    public SearchRecyclerVAdapter(Context context, ArrayList<String> getSearchImageDB, ArrayList<String> getSTitleDB, ArrayList<String> getSContentDB, ArrayList<String> getSYoutube_list, ArrayList<String> getSClickRate_list, ArrayList<String> getSDocumentID_list, ArrayList<String> placesStarRate_list, ArrayList<String> placesCoordinate_list) {
        this.context = context;
        this.getSearchImageDB = getSearchImageDB;
        this.getSTitleDB = getSTitleDB;
        this.getSContentDB = getSContentDB;
        this.getSYoutube_list = getSYoutube_list;
        this.getSClickRate_list = getSClickRate_list;
        this.getSDocumentID_list = getSDocumentID_list;
        this.placesStarRate_list = placesStarRate_list;
        this.placesCoordinate_list = placesCoordinate_list;
    }

    @NonNull
    @Override
    public SearchRecyclerVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.from(context).inflate(R.layout.item_search_getfirebase,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.titleSearch.setText(getSTitleDB.get(position));
        holder.contentSearch.setText(getSContentDB.get(position));
        Picasso.get().load(getSearchImageDB.get(position)).into(holder.searchImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("SkenImageIntent", getSearchImageDB.get(position));
                intent.putExtra("StitleIntent", getSTitleDB.get(position));
                intent.putExtra("SlocationIntent", getSTitleDB.get(position));
                intent.putExtra("ScontentIntent", getSContentDB.get(position));
                intent.putExtra("SyoutubeIntent", getSYoutube_list.get(position));
                intent.putExtra("SclickRate", getSClickRate_list.get(position));
                intent.putExtra("SdocumentId", getSDocumentID_list.get(position));
                intent.putExtra("SstarRate", placesStarRate_list.get(position));
                intent.putExtra("Scoordinate", placesCoordinate_list.get(position));
                intent.putExtra("cameHome","1");

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return getSTitleDB.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView searchImage;
        TextView titleSearch, contentSearch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            searchImage = itemView.findViewById(R.id.getImageFirebaseSearch);
            titleSearch = itemView.findViewById(R.id.getTitleFirebaseSearch);
            contentSearch = itemView.findViewById(R.id.getContentFirebaseSearch);
        }
    }

}
