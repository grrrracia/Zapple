package id.ac.umn.zapplemobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AddAReviewActivity extends AppCompatActivity {
    ConstraintLayout clUploadImage;
    ImageView imgReviewImage, btnSaveReview, btnBackFromAddReview;
    TextView tvAddPicture;
    EditText etWriteReview, etMyRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_review);

        tvAddPicture = findViewById(R.id.tvAddPict);
        etWriteReview = findViewById(R.id.etWriteReview);
        etMyRating = findViewById(R.id.etMyRating);
        btnSaveReview = findViewById(R.id.btnSaveReview);
        btnBackFromAddReview = findViewById(R.id.btnQuitAddReview);

        btnBackFromAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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