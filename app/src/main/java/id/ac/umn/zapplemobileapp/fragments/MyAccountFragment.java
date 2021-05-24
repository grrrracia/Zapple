package id.ac.umn.zapplemobileapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import id.ac.umn.zapplemobileapp.Adapter;
import id.ac.umn.zapplemobileapp.ContentActivity;
import id.ac.umn.zapplemobileapp.R;
import id.ac.umn.zapplemobileapp.RestaurantModel;
import id.ac.umn.zapplemobileapp.SearchActivity;
import id.ac.umn.zapplemobileapp.UserModel;
import id.ac.umn.zapplemobileapp.apihelper.BaseApiService;
import id.ac.umn.zapplemobileapp.apihelper.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountFragment extends Fragment{

    private MyAccountViewModel mViewModel;
    private TextView btnEditProfile;
    private TextView tvName;
    ImageView ivProfile;

    Context mContext;
    private SharedPreferences sharedpreferences;
    private String sharedPrefFile;

    BaseApiService mApiService;
    private String accessToken;
    private final String ACCESSTOKEN_KEY = "accessToken";

    Call<ArrayList<UserModel>> callPost;
    ArrayList<UserModel> hasilPost;

    public static MyAccountFragment newInstance() {
        return new MyAccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        ContentActivity contentActivity = (ContentActivity) getActivity();
        contentActivity.pageTitle.setText("Edit Profile");
        seedData(view);
        setup();
        btnEditProfile = view.findViewById(R.id.tvEditProfile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment editProfileFragment = new EditProfile();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, editProfileFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    private void seedData(View view) {
        mContext = getActivity();
        tvName = view.findViewById(R.id.tvFullName);
        ivProfile = view.findViewById(R.id.ProfilePicture);
        mApiService = UtilsApi.getAPIService();
        sharedPrefFile = mContext.getPackageName();
        sharedpreferences = mContext.getSharedPreferences(sharedPrefFile, mContext.MODE_PRIVATE);
        tvName.setText(sharedpreferences.getString("name","Zappler Zapplists"));
        accessToken = sharedpreferences.getString(ACCESSTOKEN_KEY, "0");
    }

    private void setup(){
        callPost = mApiService.getUser(accessToken);
        callPost.enqueue(new Callback<ArrayList<UserModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserModel>> call, Response<ArrayList<UserModel>> response) {
                hasilPost = response.body();
                if(hasilPost.get(0).getProfile_picture()!=null){
                    String url = hasilPost.get(0).getProfile_picture();
                    //Create another thread to get image from specific link
                    Bitmap bitmap;
                    SetImage img = new SetImage(url);
                    Thread thread = new Thread(img);
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bitmap = img.getBitmap();
                    ivProfile.setImageBitmap(bitmap);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {
                Toast.makeText(mContext, "Gagal!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private class SetImage implements Runnable{
        String url;
        Bitmap bitmap;
        SetImage(String url){
            this.url = url;
        }

        public void run(){
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
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

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(MyAccountViewModel.class);
//        // TODO: Use the ViewModel
//    }

}