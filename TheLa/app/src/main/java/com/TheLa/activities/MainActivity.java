package com.TheLa.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.TheLa.adapters.ViewPagerAdapter;
import com.TheLa.fragments.widget.CustomViewPager;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;
import com.example.TheLa.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private CustomViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private int previousItemIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewPager = binding.viewPager;
        viewPager.setPagingEnabled(false);
        bottomNavigationView = binding.bottomNav;


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                previousItemIndex = i; // Cập nhật vị trí
                switch (i) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_store).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menu_orders).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menu_me).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int selectedItemIndex = previousItemIndex;

                if (item.getItemId() == R.id.menu_home) {
                    selectedItemIndex = 0;
                } else if (item.getItemId() == R.id.menu_store) {
                    selectedItemIndex = 1;
                } else if (item.getItemId() == R.id.menu_orders) {
                    if (!handleLoginCheck()) {
                        // Nếu chưa đăng nhập, quay lại vị trí trước đó
                        viewPager.setCurrentItem(selectedItemIndex);
                        return false;
                    }
                    selectedItemIndex = 2;
                } else if (item.getItemId() == R.id.menu_me) {
                    selectedItemIndex = 3;
                }
                viewPager.setCurrentItem(selectedItemIndex);
                return true;
            }
        });
    }

    private boolean handleLoginCheck() {
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(this);
        if (!preferenceManager.isLoggedIn()) {
            Toast.makeText(this, "Đăng nhập ngay để tiếp tục trải nghiệm thú vị nào!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("previousPage", "orders"); // Ghi lại trang trước đó
                startActivity(intent);
            }, 2000); // Trì hoãn 2000ms
            return false;
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String navigateTo = intent.getStringExtra("navigateTo");
        if (navigateTo != null) {
            switch (navigateTo) {
                case "orders":
                    viewPager.setCurrentItem(2); // Chuyển đến tab "Orders"
                    break;
                case "store":
                    viewPager.setCurrentItem(1); // Chuyển đến tab "Store"
                    break;
                case "home":
                    viewPager.setCurrentItem(0); // Chuyển đến tab "Home"
                    break;
                case "me":
                    viewPager.setCurrentItem(3); // Chuyển đến tab "Me"
                    break;
            }
        }
    }

    public void setBottomNavigationVisibility(boolean isVisible) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        if (isVisible) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    public BottomNavigationView getBottomNavigationView() {
        return findViewById(R.id.bottomNav); // ID của BottomNavigationView trong layout
    }

}
