package com.fatihkurekci.ninetyplus.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fatihkurekci.ninetyplus.MainActivity;
import com.fatihkurekci.ninetyplus.R;
import com.fatihkurekci.ninetyplus.ui.onboarding.GetStarted;
import com.fatihkurekci.ninetyplus.ui.onboarding.NavigationActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 3000; // 3 seconds
    private static final String PREFS_NAME = "MyPrefs";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String LAST_LOGIN_TIME = "lastLoginTime";
    private static final long ONE_MINUTE_MILLIS = 600000; // 1 minute in milliseconds

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);

        if (isFirstTime) {
            // Eğer uygulama ilk kez başlatılıyorsa, NavigationActivity'yi başlat
            Intent intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);

            // Artık ilk kez başlatılmadığını kaydedin
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, false);
            editor.apply();
        } else {
            // Eğer uygulama daha önce başlatılmışsa, zaman kontrolü yap
            checkLastLoginTime();
        }

        // Bu aktiviteyi sonlandırın
        finish();
    }

    private void checkLastLoginTime() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        long lastLoginTime = preferences.getLong(LAST_LOGIN_TIME, 0);
        long currentTime = System.currentTimeMillis();

        if ((currentTime - lastLoginTime) <= ONE_MINUTE_MILLIS) {
            // Eğer son giriş 1 dakika içinde yapıldıysa, MainActivity'ye geç
            navigateToMain();
        } else {
            // Eğer son giriş 1 dakikadan uzun bir süre önce yapıldıysa, GetStartedActivity'ye geç
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
