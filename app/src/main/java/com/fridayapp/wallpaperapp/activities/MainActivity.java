package com.fridayapp.wallpaperapp.activities;

import android.os.Bundle;
import android.view.View;

import com.fridayapp.wallpaperapp.R;
import com.fridayapp.wallpaperapp.utils.DarkModePrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (new DarkModePrefManager(this).isNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            View decorView = getWindow().getDecorView(); //set status background black
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //set status text  light
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
//    }
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
//    }
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}