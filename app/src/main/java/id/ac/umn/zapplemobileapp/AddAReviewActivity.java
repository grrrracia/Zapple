package id.ac.umn.zapplemobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AddAReviewActivity extends AppCompatActivity {
    ConstraintLayout clUploadImage;
    TextView tvAddPicture,tvMyRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_review);

        tvMyRating = findViewById(R.id.tvMyRating);

//        Bundle restaurantData = new Bundle();
//        int restoId = getArguments().getInt("restaurantId");
//        tvMyRating.setText(restoId);


        tvAddPicture = findViewById(R.id.tvAddPict);
//        tvAddPicture.setText(restoId);


        clUploadImage = findViewById(R.id.reviewImage);
        clUploadImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Upload Image
//                clUploadImage.setBackgroundColor(-NEW PICTURE-);
                tvAddPicture.setVisibility(View.GONE);
            }
        });
    }
}