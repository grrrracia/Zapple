package id.ac.umn.zapplemobileapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import id.ac.umn.zapplemobileapp.Adapter;
import id.ac.umn.zapplemobileapp.ContentActivity;
import id.ac.umn.zapplemobileapp.R;
import id.ac.umn.zapplemobileapp.RestaurantModel;
import id.ac.umn.zapplemobileapp.SearchActivity;
import id.ac.umn.zapplemobileapp.apihelper.BaseApiService;
import id.ac.umn.zapplemobileapp.apihelper.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteRestaurants extends Fragment {
    Context mContext;
    RecyclerView recyclerView;
    BaseApiService mApiService;
    Call<ArrayList<RestaurantModel>> callPost;
    ArrayList<RestaurantModel> hasilPost;
    private SharedPreferences sharedpreferences;
    private String sharedPrefFile;
    private String accessToken;
    private final String ACCESSTOKEN_KEY = "accessToken";

    public static FavouriteRestaurants newInstance() {
        return new FavouriteRestaurants();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite_restaurant, container, false);
        ContentActivity contentActivity = (ContentActivity) getActivity();
        contentActivity.pageTitle.setText("Favourite Restaurants");
        mContext = getActivity();
        recyclerView = view.findViewById(R.id.recyclerViewFavourite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        seedData();
        setupLayout();
        return view;
    }

    private void seedData() {
        mApiService = UtilsApi.getAPIService();
        sharedPrefFile = mContext.getPackageName();
        sharedpreferences = getActivity().getSharedPreferences(sharedPrefFile, mContext.MODE_PRIVATE);
        accessToken = sharedpreferences.getString(ACCESSTOKEN_KEY, "0");
    }

    private void setupLayout() {
        callPost = mApiService.getUserFavourite(accessToken);

        callPost.enqueue(new Callback<ArrayList<RestaurantModel>>() {
            @Override
            public void onResponse(Call<ArrayList<RestaurantModel>> call, Response<ArrayList<RestaurantModel>> response) {
                hasilPost = response.body();
                System.out.println("hasil" + hasilPost);
                recyclerView.setAdapter(new Adapter(hasilPost, mContext, accessToken));
            }
            @Override
            public void onFailure(Call<ArrayList<RestaurantModel>> call, Throwable t) {
                Toast.makeText(getActivity(), "Gagal!", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(FavouriteRestaurantsViewModel.class);
//        // TODO: Use the ViewModel
//    }

}