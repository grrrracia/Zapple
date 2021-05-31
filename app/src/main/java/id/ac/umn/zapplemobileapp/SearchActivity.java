package id.ac.umn.zapplemobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import id.ac.umn.zapplemobileapp.apihelper.BaseApiService;
import id.ac.umn.zapplemobileapp.apihelper.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    Context mContext;
    RecyclerView recyclerView;
    EditText etSearchBar;
    BaseApiService mApiService;
    String search;
    Integer genre;
    Call<ArrayList<RestaurantModel>> callPost;
    Intent intent;
    ArrayList<RestaurantModel> hasilPost;
    ImageView ivSearch, btnBackFromSearch;

    private SharedPreferences sharedpreferences;
    private String sharedPrefFile;
    private String accessToken;
    private final String ACCESSTOKEN_KEY = "accessToken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        seedData();
        seedListener();
        setupLayout();
    }

    private void seedData() {
        mContext = this;
        etSearchBar = findViewById(R.id.etSearchBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mApiService = UtilsApi.getAPIService();
        ivSearch = findViewById(R.id.ic_searchicon);
        search = etSearchBar.getText().toString();

        btnBackFromSearch = findViewById(R.id.btnBackFromSearch);

        sharedPrefFile = mContext.getPackageName();
        sharedpreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        accessToken = sharedpreferences.getString(ACCESSTOKEN_KEY, "0");
    }

    private void seedListener() {
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = etSearchBar.getText().toString();
                recyclerView.removeAllViewsInLayout();
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                setupLayout();
            }
        });

        btnBackFromSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    

    private void setupLayout() {
        intent = getIntent();

        if(intent.hasExtra("genre")){

            genre = intent.getExtras().getInt("genre");
            if(search.equals("")){
                callPost = mApiService.getList(accessToken, genre);
            }else{
                callPost = mApiService.getList(accessToken, genre, search);
            }
            callPost.enqueue(new Callback<ArrayList<RestaurantModel>>() {
                @Override
                public void onResponse(Call<ArrayList<RestaurantModel>> call, Response<ArrayList<RestaurantModel>> response) {
                    hasilPost = response.body();
                    recyclerView.setAdapter(new Adapter(hasilPost, mContext, accessToken));
                }
                @Override
                public void onFailure(Call<ArrayList<RestaurantModel>> call, Throwable t) {
                    Toast.makeText(SearchActivity.this, "Gagal!", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            if(search.equals("")){
                callPost = mApiService.getList(accessToken);
            }else{
                callPost = mApiService.getList(accessToken, search);
            }
            callPost.enqueue(new Callback<ArrayList<RestaurantModel>>() {
                @Override
                public void onResponse(Call<ArrayList<RestaurantModel>> call, Response<ArrayList<RestaurantModel>> response) {
                    hasilPost = response.body();
                    System.out.println("hasil" + hasilPost);
                    recyclerView.setAdapter(new Adapter(hasilPost, mContext, accessToken));

                }
                @Override
                public void onFailure(Call<ArrayList<RestaurantModel>> call, Throwable t) {
                    Toast.makeText(SearchActivity.this, "Gagal!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
};