package com.tolgahanoktay.panaromia.recyclerviews;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tolgahanoktay.panaromia.CategoryModel;
import com.tolgahanoktay.panaromia.R;
import com.tolgahanoktay.panaromia.fragments.FragmentExplore;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecyclerVAdapter extends RecyclerView.Adapter<CategoryRecyclerVAdapter.ViewHolder> {

    Context context;
    List<CategoryModel> categoryModels;
    LayoutInflater layoutInflater;
    BottomNavigationView bottomNavigationView;
    FragmentExplore fragmentExplore;

    FirebaseFirestore firebaseFirestore;

    /*CategoryNewsRecyclerVAdapter CNews_RecyclerVAdapter;

    ArrayList<String> placesImageFBase;
    ArrayList<String> placesTitleFBase;
    ArrayList<String> placesExplainFBase;
    ArrayList<String> placesYoutubeFBase;
    ArrayList<String> placesClickRateFBase;
    ArrayList<String> placesDocumentIDFBase;*/


    public CategoryRecyclerVAdapter(Context context, List<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
    }

    @NonNull
    @Override
    public CategoryRecyclerVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.from(context).inflate(R.layout.item_category,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        CategoryModel cModel = categoryModels.get(position);
        holder.imageView.setImageResource(cModel.getIcon());
        holder.textView.setText(cModel.getCategory_name());

        if (position == 0){
            holder.layoutCategory.setBackgroundColor(ContextCompat.getColor(context,R.color.pos1));
        }else if (position  == 1){
            holder.layoutCategory.setBackgroundColor(ContextCompat.getColor(context,R.color.pos2));
        }else if (position  == 2){
            holder.layoutCategory.setBackgroundColor(ContextCompat.getColor(context,R.color.pos3));
        }else if (position == 3){
            holder.layoutCategory.setBackgroundColor(ContextCompat.getColor(context,R.color.pos4));
        }else if (position == 4){
            holder.layoutCategory.setBackgroundColor(ContextCompat.getColor(context,R.color.pos5));
        }else {
            holder.layoutCategory.setBackgroundColor(ContextCompat.getColor(context,R.color.pos));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (position == 0){
                    String name = "Art and Culture";

                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    fragmentExplore = new FragmentExplore();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment,fragmentExplore).addToBackStack(null).commit();

                    Bundle result = new Bundle();
                    result.putString("categoryName", "Art and Culture");
                    fragmentExplore.setArguments(result);

                    bottomNavigationView= appCompatActivity.findViewById(R.id.navigation_bottom);
                    bottomNavigationView.getMenu().findItem(R.id.nav_menu_explore).setChecked(true);



                }else if (position == 1){

                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    fragmentExplore = new FragmentExplore();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment,fragmentExplore).addToBackStack(null).commit();

                    Bundle result = new Bundle();
                    result.putString("categoryName", "Summer Holiday");
                    fragmentExplore.setArguments(result);

                    bottomNavigationView= appCompatActivity.findViewById(R.id.navigation_bottom);
                    bottomNavigationView.getMenu().findItem(R.id.nav_menu_explore).setChecked(true);

                }else if (position == 2){

                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    FragmentExplore fragmentExplore = new FragmentExplore();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment,fragmentExplore).addToBackStack(null).commit();

                    Bundle result = new Bundle();
                    result.putString("categoryName", "Cruise Holiday");
                    fragmentExplore.setArguments(result);

                    bottomNavigationView= appCompatActivity.findViewById(R.id.navigation_bottom);
                    bottomNavigationView.getMenu().findItem(R.id.nav_menu_explore).setChecked(true);

                }else if (position == 3){

                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    FragmentExplore fragmentExplore = new FragmentExplore();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment,fragmentExplore).addToBackStack(null).commit();

                    Bundle result = new Bundle();
                    result.putString("categoryName", "Skiing Holiday");
                    fragmentExplore.setArguments(result);

                    bottomNavigationView= appCompatActivity.findViewById(R.id.navigation_bottom);
                    bottomNavigationView.getMenu().findItem(R.id.nav_menu_explore).setChecked(true);


                }else if (position == 4){

                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    FragmentExplore fragmentExplore = new FragmentExplore();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment,fragmentExplore).addToBackStack(null).commit();

                    Bundle result = new Bundle();
                    result.putString("categoryName", "Hiking Holiday");
                    fragmentExplore.setArguments(result);

                    bottomNavigationView= appCompatActivity.findViewById(R.id.navigation_bottom);
                    bottomNavigationView.getMenu().findItem(R.id.nav_menu_explore).setChecked(true);


                }else{
                    System.out.println("Exception");
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        LinearLayout layoutCategory;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.category_name);
            imageView = itemView.findViewById(R.id.category_icon);
            layoutCategory = itemView.findViewById(R.id.category_linear);



            firebaseFirestore = FirebaseFirestore.getInstance();


        }

        /*public void bind(CategoryModel categoryModel,String[] colors,Integer position){
            itemView.setBackgroundColor(Color.parseColor(colors[position % 5]));
            textView = itemView.findViewById(R.id.category_name);
            imageView = itemView.findViewById(R.id.category_icon);
        }*/
    }
}
