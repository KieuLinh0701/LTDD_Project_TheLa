package com.TheLa.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.TheLa.presenter.UserPresenter;
import com.example.TheLa.databinding.ActivityRegistrationVerificationBinding;

public class RegistrationVerificationActivity extends AppCompatActivity {
    ActivityRegistrationVerificationBinding binding;
    UserPresenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        userPresenter = new ViewModelProvider(this).get(UserPresenter.class);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        addEvents();
    }

    private void addEvents() {

    }
}
