package com.fatihkurekci.ninetyplus.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fatihkurekci.ninetyplus.MainActivity;
import com.fatihkurekci.ninetyplus.R;
import com.fatihkurekci.ninetyplus.ui.activity.LoginActivity;
import com.fatihkurekci.ninetyplus.ui.activity.SignUpActivity;

public class GetStarted extends AppCompatActivity {
    Button loginButton;
    Button registerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLogin();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSignUp();
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class); // Buraya LoginActivity.class gibi bir intent belirtmelisiniz
        startActivity(intent);
        finish();
    }

    private void navigateToSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class); // Buraya SignUpActivity.class gibi bir intent belirtmelisiniz
        startActivity(intent);
        finish();
    }
}
