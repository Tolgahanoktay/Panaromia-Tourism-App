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

public class FavoriesRecyclerVAdapter extends RecyclerView.Adapter<FavoriesRecyclerVAdapter.ViewHolder> {

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

    public FavoriesRecyclerVAdapter(Context context, ArrayList<String> placesImage_list, ArrayList<String> placesTitle_list, ArrayList<String> placesContent_list, ArrayList<String> placesYoutube_list, ArrayList<String> placesClickRate_list, ArrayList<String> placesDocumentID_list, ArrayList<String> placesStarRate_list, ArrayList<String> placesCoordinate_list) {
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
    public FavoriesRecyclerVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.from(context).inflate(R.layout.item_take_favs_sqlite,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.titleSearch.setText(placesTitle_list.get(position));
        holder.contentSearch.setText(placesContent_list.get(position));
        Picasso.get().load(placesImage_list.get(position)).into(holder.searchImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("FavkenImageIntent", placesImage_list.get(position));
                intent.putExtra("FavtitleIntent", placesTitle_list.get(position));
                intent.putExtra("FavlocationIntent", placesTitle_list.get(position));
                intent.putExtra("FavcontentIntent", placesContent_list.get(position));
                intent.putExtra("FavyoutubeIntent", placesYoutube_list.get(position));
                intent.putExtra("FavclickRate", placesClickRate_list.get(position));
                intent.putExtra("FavdocumentId", placesDocumentID_list.get(position));
                intent.putExtra("FavstarRate", placesStarRate_list.get(position));
                intent.putExtra("Favcoordinate", placesCoordinate_list.get(position));
                intent.putExtra("cameHome","2");

                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return placesTitle_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView searchImage;
        TextView titleSearch, contentSearch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            searchImage = itemView.findViewById(R.id.getImageSqlite);
            titleSearch = itemView.findViewById(R.id.getTitleSqlite);
            contentSearch = itemView.findViewById(R.id.getContentSqlite);
        }
    }
}
