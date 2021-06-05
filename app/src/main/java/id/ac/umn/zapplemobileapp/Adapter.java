package id.ac.umn.zapplemobileapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import id.ac.umn.zapplemobileapp.apihelper.BaseApiService;
import id.ac.umn.zapplemobileapp.apihelper.UtilsApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    ArrayList<RestaurantModel> restaurantModels;
    BaseApiService mApiService;
    String search;
    Context mContext;
    FavouriteModel hasil;
    String accessToken;
    private JSONObject jsonRESULTS;

    private SharedPreferences sharedpreferences;
    private String sharedPrefFile;
    private final String RESTAURANTID_KEY = "RestaurantID";

    public Adapter(ArrayList<RestaurantModel> posts, Context mContext, String accessToken) {
        mApiService = UtilsApi.getAPIService();
        restaurantModels = posts;
        this.mContext = mContext;
        this.accessToken = accessToken;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant_overview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Create another thread to get image from specific link
        Bitmap bitmap;
        SetImage img = new SetImage(position);
        Thread thread = new Thread(img);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bitmap = img.getBitmap();

//        Boolean isFav;
//        checkUserFav check = new checkUserFav(position);
//        Thread thread2 = new Thread(check);
//        thread2.start();
//        try {
//            thread2.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //Set Data to Layout
        holder.rvMainImage.setImageBitmap(bitmap);
        holder.rvRestoName.setText(restaurantModels.get(position).getName());
        holder.rvLocation.setText(restaurantModels.get(position).getAddress());
        holder.rvMoney.setText(restaurantModels.get(position).getAvg_price().toString());
        Double rating;
        if(restaurantModels.get(position).getRating()!=null){
            rating = restaurantModels.get(position).getRating();
        }else{
            rating = 0.0;
        }
        holder.rvRating.setText(rating + " out of 5");

        if(restaurantModels.get(position).getFavourite()){
            holder.icHeart.setImageResource(R.drawable.hearticon);
        }else{
            holder.icHeart.setImageResource(R.drawable.emptyhearticon);
        }
        holder.icHeart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!restaurantModels.get(position).getFavourite()){
                    restaurantModels.get(position).setFavourite(true);
                    setFavourite(restaurantModels.get(position).getRestaurantID());
                    holder.icHeart.setImageResource(R.drawable.hearticon);
                }else{
                    restaurantModels.get(position).setFavourite(false);
                    unsetFavourite(restaurantModels.get(position).getRestaurantID());
                    holder.icHeart.setImageResource(R.drawable.emptyhearticon);
                }
            }
        });
        //Set Intent go to detail
        holder.llRestaurantItem.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Context context = mContext.getApplicationContext();
                Intent intent = new Intent(mContext, EachRestaurantActivity.class);
                intent.putExtra("restoID", restaurantModels.get(position).getRestaurantID());
                mContext.startActivity(intent);
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


    private class SetImage implements Runnable{
        Integer position;
        Bitmap bitmap;
        SetImage(Integer position){
            this.position = position;
        }

        public void run(){
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(restaurantModels.get(position).getRestaurant_photos()[0]).getContent());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public Bitmap getBitmap(){
            return bitmap;
        }
    }


    @Override
    public int getItemCount() {
        return restaurantModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView rvMainImage, icHeart;
        TextView rvRestoName, rvLocation, rvMoney, rvRating;
        LinearLayout llRestaurantItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rvMainImage = itemView.findViewById(R.id.itemMainImage);
            rvRestoName = itemView.findViewById(R.id.tvRestoName);
            rvLocation = itemView.findViewById(R.id.tvLocation);
            rvMoney = itemView.findViewById(R.id.tvMoney);
            rvRating = itemView.findViewById(R.id.tvRating);
            icHeart = itemView.findViewById(R.id.icHeart);
            llRestaurantItem = itemView.findViewById(R.id.llRestaurantItem);
        }
    }
}
