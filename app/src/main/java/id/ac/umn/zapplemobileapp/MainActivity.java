package id.ac.umn.zapplemobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.ac.umn.zapplemobileapp.apihelper.BaseApiService;
import id.ac.umn.zapplemobileapp.apihelper.UtilsApi;
import id.ac.umn.zapplemobileapp.fragments.HomeFragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class MainActivity extends AppCompatActivity {
    //General
    Context mContext;
    //Layout
    Handler handler;
    Intent intent;
    private JSONObject jsonRESULTS;
    BaseApiService mApiService;
    //Session
    private SharedPreferences sharedpreferences;
    private String sharedPrefFile;
    private String accessToken, email, password;
    private final String ACCESSTOKEN_KEY = "accessToken";
    private final String NAME_KEY = "name";
    private final String EMAIL_KEY = "email";
    private final String PASSWORD_KEY = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seedData();
        seedIntent();
    }

    public void seedData(){
        //General
        mContext = this;
        //Layout
        handler = new Handler();
        intent = new Intent(MainActivity.this, LandingPageActivity.class);
        //Session
        sharedPrefFile = mContext.getPackageName();
        sharedpreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        accessToken = sharedpreferences.getString(ACCESSTOKEN_KEY, "0");
        email = sharedpreferences.getString(EMAIL_KEY, "0");
        password = sharedpreferences.getString(PASSWORD_KEY, "0");
        mApiService = UtilsApi.getAPIService();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                return;
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                return;
            }
            if (checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE},1);
                return;
            }
        }
    }
    public void seedIntent(){
        if(email == "0"){
            intent = new Intent(mContext, LandingPageActivity.class);
        }else{
            requestLogin();
        }
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(r, 2000);
    }

    private void requestLogin() {
        //Create Login Post Request
        mApiService.loginRequest(email, password)
                .enqueue(new Callback<ResponseBody>() {
                    //Check Response
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        //If The Response is Succesful
                        if (response.isSuccessful()) {
                            try {
                                jsonRESULTS = new JSONObject(response.body().string());
                                Toast.makeText(mContext, "LOGIN SUCCESSFUL!!!", Toast.LENGTH_SHORT).show();
                                //Set Data for Shared Preferences
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(ACCESSTOKEN_KEY, jsonRESULTS.getString("accessToken"));
                                editor.putString(NAME_KEY, jsonRESULTS.getString("name"));
                                editor.putString(EMAIL_KEY, email);
                                editor.putString(PASSWORD_KEY, password);
                                editor.commit();
                                //Go to the next Activity
                                intent = new Intent(mContext, ContentActivity.class);
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
                    //If Error
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
}