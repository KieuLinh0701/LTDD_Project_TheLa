package com.TheLa.activities;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.TheLa.adapters.FullScreenImageAdapter;
import com.example.TheLa.R;

import java.util.List;

public class FullScreenImageActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ImageButton btnClose;
    private FullScreenImageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        initializeViews();

        setupEventHandlers();

        setupViewPager();
    }

    private void setupViewPager() {
        // Nhận danh sách ảnh và vị trí hiện tại từ Intent
        List<String> imageList = getIntent().getStringArrayListExtra("imageList");
        int currentPosition = getIntent().getIntExtra("currentPosition", 0);

        // Thiết lập adapter cho ViewPager
        adapter = new FullScreenImageAdapter(this, imageList);
        viewPager.setAdapter(adapter);

        // Di chuyển đến vị trí ảnh hiện tại
        viewPager.setCurrentItem(currentPosition, false);
    }

    private void setupEventHandlers() {
        btnClose.setOnClickListener(v -> finish());
    }

    private void initializeViews() {
        viewPager = findViewById(R.id.viewPager);
        btnClose = findViewById(R.id.btnClose);
    }
}