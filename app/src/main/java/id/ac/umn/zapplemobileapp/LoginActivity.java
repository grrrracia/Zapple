package id.ac.umn.zapplemobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.ac.umn.zapplemobileapp.apihelper.BaseApiService;
import id.ac.umn.zapplemobileapp.apihelper.UtilsApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    //General
    Context mContext;
    String email, password;
    //Layout_Related
    EditText etEmail, etPassword;
    ImageView ivLogin;
    TextView tvSignUp;
    ProgressDialog loading;
    //API_Related
    private JSONObject jsonRESULTS;
    BaseApiService mApiService;
    //Session_Related
    private SharedPreferences sharedpreferences;
    private String sharedPrefFile, accessToken;

    private final String ACCESSTOKEN_KEY = "accessToken";
    private final String NAME_KEY = "name";
    private final String EMAIL_KEY = "email";
    private final String PASSWORD_KEY = "password";

    TextView signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize
        seedData();
        seedButton();
    }

    private void seedData() {
        //General
        mContext = this;
        //Layout
        etEmail = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        ivLogin = findViewById(R.id.btnLogin);
        tvSignUp = (TextView) findViewById(R.id.tvSignup);
        //Preferences
        sharedPrefFile = mContext.getPackageName();
        sharedpreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        accessToken = sharedpreferences.getString(ACCESSTOKEN_KEY, "0");
        mApiService = UtilsApi.getAPIService();
    }

    private void seedButton() {
        //Login Button
        ivLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestLogin();
            }
        });
        //SignUp Button
        tvSignUp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }


    private void requestLogin() {
        //Login Data
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        //Create Login Post Request
        mApiService.loginRequest(email, password)
                .enqueue(new Callback<ResponseBody>() {
                    //Check Response
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        //If The Response is Succesful
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                jsonRESULTS = new JSONObject(response.body().string());
                                Toast.makeText(mContext, "WELCOME BACK!!!", Toast.LENGTH_SHORT).show();
                                //Set Data for Shared Preferences
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(ACCESSTOKEN_KEY, jsonRESULTS.getString("accessToken"));
                                editor.putString(NAME_KEY, jsonRESULTS.getString("name"));
                                editor.putString(EMAIL_KEY, email);
                                editor.putString(PASSWORD_KEY, password);
                                editor.commit();
                                //Go to the next Activity
                                Intent intent = new Intent(mContext, ContentActivity.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
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
                        loading.dismiss();
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finishActivity(0);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity(0);
        finish();
    }
}