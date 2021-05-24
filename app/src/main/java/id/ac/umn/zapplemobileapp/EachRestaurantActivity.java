package id.ac.umn.zapplemobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

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
    Intent intent;
    Context mContext;
    Call<ArrayList<RestaurantModel>> callPost;
    private SharedPreferences sharedpreferences;
    private String sharedPrefFile;
    private String accessToken;
    private final String ACCESSTOKEN_KEY = "accessToken";
    private Integer restaurantID;
    ArrayList<RestaurantModel> hasilPost;
    TextView tvRestoName, tvAddress, tvPriceRange, tvRating;
    ImageView icHeart;
    Boolean curr;

    BaseApiService mApiService;
    private JSONObject jsonRESULTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_restaurant);

        tvRestoName = findViewById(R.id.tvRestaurantName);
        tvAddress = findViewById(R.id.tvLocation);
        tvPriceRange = findViewById(R.id.tvMoney);
        tvRating = findViewById(R.id.tvRating);
        icHeart = findViewById(R.id.icFavourite);
        seedData(view);
        setupLayout();
        if(curr){
            icHeart.setImageResource(R.drawable.hearticon);
        }else{
            icHeart.setImageResource(R.drawable.emptyhearticon);
        }

        icHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!curr){
                    setFavourite(getArguments().getInt("restoID"));
                    icHeart.setImageResource(R.drawable.hearticon);
                    curr = true;
                }else{
                    unsetFavourite(getArguments().getInt("restoID"));
                    icHeart.setImageResource(R.drawable.emptyhearticon);
                    curr = false;
                }
            }
        });
        int restoId = getArguments().getInt("restaurantId");

        btnCallResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+restoPhoneNumber));

                if (ContextCompat.checkSelfPermission(contentActivity.getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
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
//                Navigation.findNavController(v).navigate(R.id.action_eachRestaurant_to_addAReviewFrag);

                Bundle reviewData = new Bundle();
                reviewData.putInt("restaurantID", restoId);

                Fragment addAReviewFrag = new AddAReviewFragment();
                FragmentTransaction addReviewTransaction = getFragmentManager().beginTransaction();

                addAReviewFrag.setArguments(reviewData);

                addReviewTransaction.replace(R.id.nav_host_fragment_container, addAReviewFrag);
                addReviewTransaction.addToBackStack(null);
                addReviewTransaction.commit();
            }
        });
        return view;
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

    private void seedData(View view) {
        mContext = getActivity();
        curr = getArguments().getBoolean("isFavourite");
        sharedPrefFile = mContext.getPackageName();
        sharedpreferences = mContext.getSharedPreferences(sharedPrefFile, mContext.MODE_PRIVATE);
        if(!sharedpreferences.contains("RestaurantID")){
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_eachRestaurant);
        }else{
            accessToken = sharedpreferences.getString(ACCESSTOKEN_KEY, "0");
            restaurantID = sharedpreferences.getInt("RestaurantId", 0);
        }

        ImageSlider imageSlider = view.findViewById(R.id.imageSlider);

        List<SlideModel> slideModels = new ArrayList<>();
        for(String image: getArguments().getStringArray("restoImage")){
            slideModels.add(new SlideModel(image, getArguments().getString("restaurantName")));
        }
        imageSlider.setImageList(slideModels, true);

        btnOpenMenu = view.findViewById(R.id.btnOpenMenu);
        btnCallResto = view.findViewById(R.id.btnCall);
        btnAddReview = view.findViewById(R.id.btnAddReview);
        btnOpenOnMaps = view.findViewById(R.id.btnOpenMaps);

        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.action_eachRestaurant_to_addAReviewFrag);
                Fragment aboutUsFragment = new AboutUsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                Bundle reviewData = new Bundle();
                reviewData.putInt("restaurantID", restaurantID);

                Fragment addAReviewFrag = new AddAReviewFragment();
                FragmentTransaction addReviewTransaction = getFragmentManager().beginTransaction();

                addAReviewFrag.setArguments(reviewData);

                addReviewTransaction.replace(R.id.nav_host_fragment_container, addAReviewFrag);
                addReviewTransaction.addToBackStack(null);
                addReviewTransaction.commit();
            }
        });

        btnOpenOnMaps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MapsActivity.class);
                intent.putExtra("latitude", getArguments().getString("latitude"));
                intent.putExtra("longitude", getArguments().getString("longitude"));
                startActivity(intent);
            }
        });

        mApiService = UtilsApi.getAPIService();
    }

    private void setupLayout() {

        callPost = mApiService.getDetail(accessToken,restaurantID);

        callPost.enqueue(new Callback<ArrayList<RestaurantModel>>() {
            @Override
            public void onResponse(Call<ArrayList<RestaurantModel>> call, Response<ArrayList<RestaurantModel>> response) {
                hasilPost = response.body();
                System.out.println(hasilPost);
            }
            @Override
            public void onFailure(Call<ArrayList<RestaurantModel>> call, Throwable t) {
                Toast.makeText(getActivity(), "Gagal!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}