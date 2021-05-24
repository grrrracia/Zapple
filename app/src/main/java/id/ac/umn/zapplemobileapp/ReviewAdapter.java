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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    ArrayList<ReviewModel> reviewModels;
    BaseApiService mApiService;
    String search;
    Context mContext;
    FavouriteModel hasil;
    String accessToken;
    private JSONObject jsonRESULTS;

    private SharedPreferences sharedpreferences;
    private String sharedPrefFile;
    private final String RESTAURANTID_KEY = "RestaurantID";

    public ReviewAdapter(ArrayList<ReviewModel> posts, Context mContext, String accessToken) {
        mApiService = UtilsApi.getAPIService();
        reviewModels = posts;
        this.mContext = mContext;
        this.accessToken = accessToken;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant_review, parent, false);
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

        //Set Data to Layout
        holder.ivPhoto.setImageBitmap(bitmap);
        holder.rvUser.setText(reviewModels.get(position).getName());
        holder.rvReview.setText(reviewModels.get(position).getReview());
        holder.rvRating.setText(reviewModels.get(position).getScore().toString() + " Out of 5");
    }

    private class SetImage implements Runnable{
        Integer position;
        Bitmap bitmap;
        SetImage(Integer position){
            this.position = position;
        }

        public void run(){
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(reviewModels.get(position).getMedia_link()).getContent());
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
        return reviewModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView rvUser, rvReview, rvRating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            rvUser = itemView.findViewById(R.id.tvReviewTitle);
            rvReview = itemView.findViewById(R.id.tvReviewContent);
            rvRating = itemView.findViewById(R.id.tvReviewRating);
        }
    }
}
