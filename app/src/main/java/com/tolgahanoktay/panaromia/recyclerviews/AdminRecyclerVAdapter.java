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
import com.tolgahanoktay.panaromia.UploadActivity;

import java.util.ArrayList;
import java.util.Date;

public class AdminRecyclerVAdapter extends RecyclerView.Adapter<AdminRecyclerVAdapter.ViewHolder>{

    Context context;
    LayoutInflater layoutInflater;
    private ArrayList<String> admin_image;
    private ArrayList<String> admin_placesName;
    private ArrayList<String> admin_explain;
    private ArrayList<String> admin_category;
    private ArrayList<String> admin_youtube;
    private ArrayList<String> admin_clickRate;
    private ArrayList<String> admin_adminStarRate;
    private ArrayList<Date> admin_date;
    private ArrayList<String> admin_documentName;
    private ArrayList<String> admin_coordinate;

    public AdminRecyclerVAdapter(Context context, ArrayList<String> admin_image, ArrayList<String> admin_placesName, ArrayList<String> admin_explain, ArrayList<String> admin_category,
    ArrayList<String> admin_youtube, ArrayList<String> admin_clickRate, ArrayList<String> admin_adminStarRate, ArrayList<Date> admin_date, ArrayList<String> admin_documentName, ArrayList<String> admin_coordinate) {

        this.context = context;
        this.admin_image = admin_image;
        this.admin_placesName = admin_placesName;
        this.admin_explain = admin_explain;
        this.admin_category = admin_category;
        this.admin_youtube = admin_youtube;
        this.admin_clickRate = admin_clickRate;
        this.admin_adminStarRate = admin_adminStarRate;
        this.admin_date = admin_date;
        this.admin_documentName = admin_documentName;
        this.admin_coordinate = admin_coordinate;
    }

    @NonNull
    @Override
    public AdminRecyclerVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.from(context).inflate(R.layout.item_admin_show_data,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminRecyclerVAdapter.ViewHolder holder, final int position) {
        holder.places.setText(admin_placesName.get(position));
        holder.category.setText(admin_category.get(position));
        holder.date.setText(String.valueOf(admin_date.get(position)));
        Picasso.get().load(admin_image.get(position)).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,  UploadActivity.class);
                intent.putExtra("adminImage", admin_image.get(position));
                intent.putExtra("adminPlacesName", admin_placesName.get(position));
                intent.putExtra("adminCategory", admin_category.get(position));
                intent.putExtra("adminContent", admin_explain.get(position));
                intent.putExtra("adminyYoutube", admin_youtube.get(position));
                intent.putExtra("adminClickRate", admin_clickRate.get(position));
                intent.putExtra("adminDocumentId", admin_documentName.get(position));
                intent.putExtra("adminStarRate", admin_adminStarRate.get(position));
                intent.putExtra("AdminCoordinate", admin_coordinate.get(position));
                intent.putExtra("cameAdmin","0");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return admin_placesName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView places,category,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.admin_image);
            places = itemView.findViewById(R.id.admin_placesName);
            category = itemView.findViewById(R.id.admin_category);
            date = itemView.findViewById(R.id.admin_date);
        }
    }
}