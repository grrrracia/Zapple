package id.ac.umn.zapplemobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.ac.umn.zapplemobileapp.apihelper.BaseApiService;
import id.ac.umn.zapplemobileapp.apihelper.UtilsApi;
import id.ac.umn.zapplemobileapp.fragments.AboutUsFragment;
import id.ac.umn.zapplemobileapp.fragments.AddAReviewFragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CALL_PHONE;

public class EachRestaurantActivity extends AppCompatActivity {

    ImageView btnOpenMenu, btnCallResto, btnAddReview, btnOpenOnMaps;

    ProgressDialog loading;

    Intent intent;
    Context mContext;
    Call<ArrayList<RestaurantModel>> callPost;
    Call<ArrayList<ReviewModel>> callReview;
    private SharedPreferences sharedpreferences;
    private String sharedPrefFile;
    private String accessToken;
    private final String ACCESSTOKEN_KEY = "accessToken";
    private Integer restaurantID;
    ArrayList<RestaurantModel> hasilPost;
    ArrayList<ReviewModel> hasilReview;
    RecyclerView recyclerView;
    TextView tvRestoName, tvAddress, tvPriceRange, tvRating;
    ImageView icHeart;
    ImageView btnBackFromResto;
    TextView headerRestoName;

    String[] image;
    String name, address, phone, latitude, longitude;
    Integer price;
    Double rating;
    Boolean isFavourite;

    BaseApiService mApiService;
    private JSONObject jsonRESULTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_restaurant);
        seedData();
        loading = ProgressDialog.show(mContext, null, "Please Wait...", true, false);
        btnBackFromResto = findViewById(R.id.btnBackFromResto);
        btnBackFromResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void unsetFavourite(Integer restaurantID){
        mApiService.unsetFavourite(accessToken, restaurantID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonRESULTS = new JSONObject(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.errorBody().string());
                        String error_message = jsonRESULTS.getString("message");
                        Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    public void setFavourite(Integer id){
        mApiService.setFavourite(accessToken, id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonRESULTS = new JSONObject(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.errorBody().string());
                        String error_message = jsonRESULTS.getString("message");
                        Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    private void seedData() {
        mContext = this;
        restaurantID = getIntent().getExtras().getInt("restoID");
        sharedPrefFile = mContext.getPackageName();
        sharedpreferences = mContext.getSharedPreferences(sharedPrefFile, mContext.MODE_PRIVATE);
        accessToken = sharedpreferences.getString(ACCESSTOKEN_KEY, "0");
        mApiService = UtilsApi.getAPIService();

        tvRestoName = findViewById(R.id.tvRestaurantName);
        tvAddress = findViewById(R.id.tvLocation);
        tvPriceRange = findViewById(R.id.tvMoney);
        tvRating = findViewById(R.id.tvRating);
        icHeart = findViewById(R.id.icFavourite);

        headerRestoName = findViewById(R.id.headerRestoName);

        recyclerView = findViewById(R.id.recyclerViewReview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnOpenMenu = findViewById(R.id.btnOpenMenu);
        btnCallResto = findViewById(R.id.btnCall);
        btnAddReview = findViewById(R.id.btnAddReview);
        btnOpenOnMaps = findViewById(R.id.btnOpenMaps);

        getData();


        icHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFavourite){
                    setFavourite(restaurantID);
                    icHeart.setImageResource(R.drawable.hearticon);
                    isFavourite = true;
                }else{
                    unsetFavourite(restaurantID);
                    icHeart.setImageResource(R.drawable.emptyhearticon);
                    isFavourite = false;
                }
            }
        });

        btnCallResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+phone));

                if (ContextCompat.checkSelfPermission(EachRestaurantActivity.this, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }
//                startActivity(callIntent);
            }
        });

        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddAReviewActivity.class);
                System.out.println(intent);
                intent.putExtra("restoID", restaurantID);
                startActivity(intent);
            }
        });

        btnOpenOnMaps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String geoUriString="geo:"+latitude+","+longitude+"?q="+latitude+","+longitude;
                Uri gmmIntentUri = Uri.parse(geoUriString);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
    }

    private void getData() {

        callPost = mApiService.getDetail(accessToken,restaurantID);

        callPost.enqueue(new Callback<ArrayList<RestaurantModel>>() {
            @Override
            public void onResponse(Call<ArrayList<RestaurantModel>> call, Response<ArrayList<RestaurantModel>> response) {
                hasilPost = response.body();
                image = hasilPost.get(0).getRestaurant_photos();
                name = hasilPost.get(0).getName();
                address = hasilPost.get(0).getAddress();
                phone = hasilPost.get(0).getPhone_number();
                latitude = hasilPost.get(0).getLatitude();
                longitude = hasilPost.get(0).getLongitude();
                if(hasilPost.get(0).getRating() == null){
                    rating = 0.0;
                }else{
                    rating = hasilPost.get(0).getRating();
                }

                price = hasilPost.get(0).getAvg_price();
                isFavourite = hasilPost.get(0).getFavourite();

                tvRestoName.setText(name);
                tvAddress.setText(address);
                tvPriceRange.setText(price + " per pax");
                tvRating.setText(rating + " out of 5");

                if(isFavourite){
                    icHeart.setImageResource(R.drawable.hearticon);
                }else{
                    icHeart.setImageResource(R.drawable.emptyhearticon);
                }

                ImageSlider imageSlider = findViewById(R.id.imageSlider);

                List<SlideModel> slideModels = new ArrayList<>();
                for(String photo: image){
                    slideModels.add(new SlideModel(photo, name));
                }
                imageSlider.setImageList(slideModels, true);

                String pageHeader = name;
                String[] result = pageHeader.split("\\s+");
                pageHeader = result[0]+" "+result[1];

                headerRestoName.setText(pageHeader);
                loading.dismiss();
            }
            @Override
            public void onFailure(Call<ArrayList<RestaurantModel>> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(EachRestaurantActivity.this, "Gagal!", Toast.LENGTH_SHORT).show();

            }
        });

        callReview = mApiService.getReview(accessToken,restaurantID);

        callReview.enqueue(new Callback<ArrayList<ReviewModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ReviewModel>> call, Response<ArrayList<ReviewModel>> response) {
                hasilReview = response.body();
                recyclerView.setAdapter(new ReviewAdapter(hasilReview, mContext, accessToken));
            }
            @Override
            public void onFailure(Call<ArrayList<ReviewModel>> call, Throwable t) {
                Toast.makeText(EachRestaurantActivity.this, "Gagal!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}