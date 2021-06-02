package id.ac.umn.zapplemobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import id.ac.umn.zapplemobileapp.apihelper.BaseApiService;
import id.ac.umn.zapplemobileapp.apihelper.UtilsApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAReviewActivity extends AppCompatActivity {
    ConstraintLayout clUploadImage;
    ImageView imgReviewImage, btnSaveReview, btnBackFromAddReview;
    TextView tvAddPicture;
    EditText etWriteReview, etMyRating;
    Integer restaurantID;
    private SharedPreferences sharedpreferences;
    private String sharedPrefFile;
    BaseApiService mApiService;
    private String accessToken;
    Context mContext;
    File file;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final String ACCESSTOKEN_KEY = "accessToken";

    Call<ResponseBody> callPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_review);
        
        seedData();
    }

    private void seedData() {
        mContext = this;
        restaurantID = getIntent().getExtras().getInt("restoID");
        mApiService = UtilsApi.getAPIService();
        tvAddPicture = findViewById(R.id.tvAddPict);
        etWriteReview = findViewById(R.id.etWriteReview);
        etMyRating = findViewById(R.id.etMyRating);
        btnSaveReview = findViewById(R.id.btnSaveReview);
        btnBackFromAddReview = findViewById(R.id.btnQuitAddReview);
        clUploadImage = findViewById(R.id.reviewImage);
        sharedPrefFile = mContext.getPackageName();
        sharedpreferences = mContext.getSharedPreferences(sharedPrefFile, mContext.MODE_PRIVATE);
        accessToken = sharedpreferences.getString(ACCESSTOKEN_KEY, "0");

        btnBackFromAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        clUploadImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
                if(takePictureIntent.resolveActivity(mContext.getPackageManager()) != null){
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

                tvAddPicture.setVisibility(View.GONE);
            }
        });

        btnSaveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReview();
            }
        });
    }

    private void saveReview() {
        String review = etWriteReview.getText().toString();
        Integer rating = Integer.parseInt(etMyRating.getText().toString());
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("photo", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        callPost = mApiService.reviewRequest(accessToken, restaurantID, review, rating, filePart);
        callPost.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody jsonResponse = response.body();
                Intent intent = new Intent(mContext, EachRestaurantActivity.class);
                intent.putExtra("restoID", restaurantID);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.getLocalizedMessage();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            file = savebitmap(bitmap);
            BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
            clUploadImage.setBackground(ob);
        }
    }



    private File savebitmap(Bitmap bmp) {
        file = new File(mContext.getCacheDir(), "temp.png");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = bmp;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}