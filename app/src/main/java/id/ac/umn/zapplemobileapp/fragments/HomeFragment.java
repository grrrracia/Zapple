package id.ac.umn.zapplemobileapp.fragments;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.ac.umn.zapplemobileapp.ContentActivity;
import id.ac.umn.zapplemobileapp.MainActivity;
import id.ac.umn.zapplemobileapp.MapsActivity;
import id.ac.umn.zapplemobileapp.MapsActivityFromHome;
import id.ac.umn.zapplemobileapp.R;
import id.ac.umn.zapplemobileapp.SearchActivity;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment implements View.OnClickListener{
    LinearLayout gotoRestoOfTheDay;
    ImageView ivCafe, ivFastFood, ivJapan, ivBBQ, ivNoodles, ivPizza, ivStreet, ivSeafood;
    ImageView mapArea;
    TextView tvWelcomeName;

    Intent intent;

    Context mContext;
    private SharedPreferences sharedpreferences;
    private String sharedPrefFile;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    ContentActivity contentActivity = (ContentActivity) getActivity();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        ContentActivity contentActivity = (ContentActivity) getActivity();
        contentActivity.pageTitle.setText("Home");
        seedData(view);
        seedListener();

//        gotoRestoOfTheDay = view.findViewById(R.id.llrestaurantOfTheDay);
//        gotoRestoOfTheDay.setOnClickListener(this);

        mapArea = view.findViewById(R.id.btnMap);
        mapArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open Map
                if (ActivityCompat.checkSelfPermission(contentActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(contentActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                } else {
                    Log.d(TAG, "getLocation: permissions granted");
                    Intent openMapsFromHome = new Intent(getActivity(), MapsActivityFromHome.class);
                    startActivity(openMapsFromHome);
                }
                Intent openMapsFromHome = new Intent(getActivity(), MapsActivityFromHome.class);
                startActivity(openMapsFromHome);
            }
        });

        return view;
    }

    private void seedData(View view) {
        mContext = getActivity();
        tvWelcomeName = view.findViewById(R.id.tvWelcomeName);
        ivCafe = view.findViewById(R.id.btnCafe);
        ivFastFood = view.findViewById(R.id.btnFastFood);
        ivJapan = view.findViewById(R.id.btnJapanese);
        ivBBQ = view.findViewById(R.id.btnBarbeque);
        ivNoodles = view.findViewById(R.id.btnNoodles);
        ivPizza = view.findViewById(R.id.btnPizza);
        ivStreet = view.findViewById(R.id.btnStreetFood);
        ivSeafood = view.findViewById(R.id.btnSeafood);

        sharedPrefFile = mContext.getPackageName();
        sharedpreferences = mContext.getSharedPreferences(sharedPrefFile, mContext.MODE_PRIVATE);
        tvWelcomeName.setText("Welcome " + sharedpreferences.getString("name", "Zappler") + "!!!");
    }


    private void seedListener() {
        ivCafe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                contentActivity.pageTitle.setText("Cafe");
                intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("genre",1);
                startActivity(intent);

            }
        });
        ivFastFood.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                contentActivity.pageTitle.setText("Fast Food");
                intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("genre",2);
                startActivity(intent);
            }
        });
        ivJapan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                contentActivity.pageTitle.setText("Japanese Food");
                intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("genre",3);
                startActivity(intent);
            }
        });
        ivBBQ.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                contentActivity.pageTitle.setText("Barbeque");
                intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("genre",4);
                startActivity(intent);
            }
        });
        ivNoodles.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                contentActivity.pageTitle.setText("Noodles");
                intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("genre",5);
                startActivity(intent);
            }
        });
        ivPizza.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                contentActivity.pageTitle.setText("Pizza");
                intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("genre",6);
                startActivity(intent);
            }
        });
        ivStreet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                contentActivity.pageTitle.setText("Street Food");
                intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("genre",7);
                startActivity(intent);
            }
        });
        ivSeafood.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                contentActivity.pageTitle.setText("Seafood");
                intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("genre",8);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View view) {
        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_eachRestaurant);
    }
}