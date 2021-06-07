package com.tolgahanoktay.panaromia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tolgahanoktay.panaromia.fragments.FragmentExplore;
import com.tolgahanoktay.panaromia.fragments.FragmentFavList;
import com.tolgahanoktay.panaromia.fragments.FragmentHome;
import com.tolgahanoktay.panaromia.fragments.FragmentSearch;

import java.util.Collections;

public class HomePage extends AppCompatActivity {

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_page);

        checkConnection();

        /*RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(Collections.singletonList("2B00CA07EDD2CAC6C420171BCE6AEAA6"))
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);*/

        navigationView = findViewById(R.id.navigation_bottom);
        navigationView.setOnNavigationItemSelectedListener(navSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment, new FragmentHome()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {

                case R.id.nav_menu_home:
                    selectedFragment = new FragmentHome();
                    break;

                case R.id.nav_menu_search:
                    selectedFragment = new FragmentSearch();
                    break;

                case R.id.nav_menu_favorite:
                    selectedFragment = new FragmentFavList();
                    break;


                case R.id.nav_menu_explore:
                    selectedFragment = new FragmentExplore();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment, selectedFragment).commit();

            return true;
        }
    };

    public void checkConnection(){
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (null!=networkInfo){
            if (networkInfo.getType()==ConnectivityManager.TYPE_WIFI){
                //Toast.makeText(this,"Wifi ",Toast.LENGTH_LONG).show();
            }
            if (networkInfo.getType()==ConnectivityManager.TYPE_MOBILE){
                //Toast.makeText(this,"Mobile ",Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this,"No Internet Connection Please Check Your Network Connection",Toast.LENGTH_LONG).show();
        }

    }

}
