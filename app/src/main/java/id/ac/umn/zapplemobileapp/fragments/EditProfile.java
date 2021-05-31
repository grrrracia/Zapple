package id.ac.umn.zapplemobileapp.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import id.ac.umn.zapplemobileapp.ContentActivity;
import id.ac.umn.zapplemobileapp.EachRestaurantActivity;
import id.ac.umn.zapplemobileapp.R;
import id.ac.umn.zapplemobileapp.UserModel;
import id.ac.umn.zapplemobileapp.apihelper.BaseApiService;
import id.ac.umn.zapplemobileapp.apihelper.UtilsApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class EditProfile extends Fragment {

    //    private EditProfileViewModel mViewModel;
    ConstraintLayout CLeditProPicture;

    TextView btnChangeProfilePicture;

    EditText etFullName, etUsername, etEmail, etPassword, etConfirmPassword;
    ImageView btnSaveChange;
    Context mContext;
    private SharedPreferences sharedpreferences;
    private String sharedPrefFile;
    BaseApiService mApiService;
    private String accessToken;
    private final String ACCESSTOKEN_KEY = "accessToken";
    private final String NAME_KEY = "name";
    private final String EMAIL_KEY = "email";
    private final String PASSWORD_KEY = "password";
    String name;
    String email;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Call<ArrayList<UserModel>> callPost;
    Call<ResponseBody> callEdit;
    ArrayList<UserModel> hasilPost;

    private static final int PERMISSION_REQUEST_CODE = 200;

    public static EditProfile newInstance() {
        return new EditProfile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        seedData(view);
        setup();


        CLeditProPicture = view.findViewById(R.id.editProPicture);
        CLeditProPicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
                if(takePictureIntent.resolveActivity(mContext.getPackageManager()) != null){
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                System.out.println("CHANGE PICTURE");
            }
        });

        return view;
    }



    private void seedData(View view) {
        CLeditProPicture = view.findViewById(R.id.editProPicture);

        etEmail = view.findViewById(R.id.etEditEmail);
        etFullName = view.findViewById(R.id.etEditFullName);
        btnSaveChange = view.findViewById(R.id.btnSaveChanges);
        mContext = getActivity();
        mApiService = UtilsApi.getAPIService();
        sharedPrefFile = mContext.getPackageName();
        sharedpreferences = mContext.getSharedPreferences(sharedPrefFile, mContext.MODE_PRIVATE);
        accessToken = sharedpreferences.getString(ACCESSTOKEN_KEY, "0");

        CLeditProPicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
                if(takePictureIntent.resolveActivity(mContext.getPackageManager()) != null){
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });

        btnSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });
    }




    private void editProfile() {
        if(etEmail.getText().toString().equals("")){
            email = sharedpreferences.getString(EMAIL_KEY, "0");
        }else{
            email = etEmail.getText().toString();
        }

        if(etFullName.getText().toString().equals("")){
            name = sharedpreferences.getString(NAME_KEY, "0");
        }else{
            name = etFullName.getText().toString();
        }

        callEdit = mApiService.editUser(accessToken, name, email);
        callEdit.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(NAME_KEY, name);
                editor.putString(EMAIL_KEY, email);
                editor.commit();
                Fragment myAccoutFragment = new MyAccountFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, myAccoutFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mContext, "Gagal!", Toast.LENGTH_SHORT).show();
            }
        });
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
                    BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                    CLeditProPicture.setBackground(ob);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {
                Toast.makeText(mContext, "Gagal!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            File file = savebitmap(bitmap);

            MultipartBody.Part filePart = MultipartBody.Part.createFormData("photo", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

            Call<ResponseBody> call = mApiService.changePP(accessToken, filePart);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody jsonResponse = response.body();
                    Intent intent = new Intent(mContext, ContentActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.getLocalizedMessage();
                }
            });
        }
    }

    private File savebitmap(Bitmap bmp) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        // String temp = null;
        File file = new File(extStorageDirectory, "temp.png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "temp.png");

        }

        try {
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
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
//        mViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
//        // TODO: Use the ViewModel
//    }


}