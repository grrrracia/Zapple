package id.ac.umn.zapplemobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LandingPageActivity extends AppCompatActivity {

    ImageView loginButton, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        loginButton = findViewById(R.id.btnLandingLogin);
        signupButton = findViewById(R.id.btnLandingSignup);

        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                startActivity(new Intent(LandingPageActivity.this, LoginActivity.class));
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                startActivity(new Intent(LandingPageActivity.this, SignUpActivity.class));
            }
        });
    }
}