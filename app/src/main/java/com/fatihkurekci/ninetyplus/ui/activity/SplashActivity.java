package com.fatihkurekci.ninetyplus.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.fatihkurekci.ninetyplus.MainActivity;
import com.fatihkurekci.ninetyplus.R;
import com.fatihkurekci.ninetyplus.ui.onboarding.GetStarted;
import com.fatihkurekci.ninetyplus.ui.onboarding.NavigationActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("MODE", MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("nightMode", false);
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_splash);
        hideSystemUI();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                checkFirstTimeLaunch();
            }
        }, SPLASH_DELAY);
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void checkFirstTimeLaunch() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean("IsFirstTimeLaunch", true);

        if (isFirstTime) {
            Intent intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("IsFirstTimeLaunch", false);
            editor.apply();
        } else {
            checkLastLoginTime();
        }

        finish();
    }

    private void checkLastLoginTime() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        long lastLoginTime = preferences.getLong("lastLoginTime", 0);
        long currentTime = System.currentTimeMillis();
        final long TEN_MINUTE_MILLIS = 600000;

        if ((currentTime - lastLoginTime) <= TEN_MINUTE_MILLIS) {
            navigateToMain();
        } else {
            navigateToGetStarted();
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToGetStarted() {
        Intent intent = new Intent(this, GetStarted.class);
        startActivity(intent);
        finish();
    }
}
