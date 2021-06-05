package id.ac.umn.zapplemobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import id.ac.umn.zapplemobileapp.fragments.AboutUsFragment;

public class ContentActivity extends AppCompatActivity{
    Context mContext;
    private AppBarConfiguration mAppBarConfiguration;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public TextView pageTitle;
    private LinearLayout btnSetting, btnAboutUs,btnLogout;

    private SharedPreferences sharedpreferences;
    private String sharedPrefFile;

    private ImageView btnSearch;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        seedData();
        seedIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!sharedpreferences.contains("accessToken")){
            intent = new Intent(mContext, LandingPageActivity.class);
            startActivity(intent);
        }
    }

    private void seedData() {
        mContext = this;
        toolbar = findViewById(R.id.myToolbar);
        drawerLayout = findViewById(R.id.DrawerLayout);
        navigationView = findViewById(R.id.NavigationView);
        pageTitle = findViewById(R.id.pageTitle);
        btnSearch = findViewById(R.id.ic_search);

        btnAboutUs = findViewById(R.id.menuAboutUs);
        btnLogout = findViewById(R.id.menuLogout);



        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_icon);

        sharedPrefFile = mContext.getPackageName();
        sharedpreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        pageTitle.setText("Zapple");
    }


    private void seedIntent() {
        btnSearch.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                intent = new Intent(mContext, SearchActivity.class);
                startActivity(intent);
            }
        });

        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment aboutUsFragment = new AboutUsFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, aboutUsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                intent = new Intent(mContext, LandingPageActivity.class);
                startActivity(intent);
            }
        });

        navigationView.setItemIconTintList(null);
        navigationView.setItemIconSize(150);
        View headerView = navigationView.getHeaderView(0);
        TextView helloName = (TextView) headerView.findViewById(R.id.tvHeaderHelloText);
        String helloUserString = "Hello "+ sharedpreferences.getString("name", "Zappler") + " !";
        helloName.setText(helloUserString);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.searchActivity,R.id.homeFragment, R.id.favouriteRestaurants, R.id.myAccountFragment
        ).setDrawerLayout(drawerLayout).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}