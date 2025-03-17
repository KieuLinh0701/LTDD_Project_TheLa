package com.TheLa.activities;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.TheLa.adapters.ViewPagerAdapter;
import com.example.TheLa.R;
import com.example.TheLa.databinding.ActivityMainBinding;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewPager = binding.viewPager;
        bottomNavigationView = binding.bottomNav;

        SetVisibleBadgeDrawableNotification();

        SetVisibleBadgeDrawableOrder();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_orders).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menu_me).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menu_notifications).setChecked(true);
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
                if (item.getItemId() == R.id.menu_home) {
                    viewPager.setCurrentItem(0);
                } else if (item.getItemId() == R.id.menu_orders) {
                    viewPager.setCurrentItem(1);
                } else if (item.getItemId() == R.id.menu_notifications) {
                    viewPager.setCurrentItem(2);
                } else if (item.getItemId() == R.id.menu_me) {
                    viewPager.setCurrentItem(3);
                }
                return true;
            }
        });
    }

    private void SetVisibleBadgeDrawableNotification() {
        // Thêm BadgeDrawable cho mục Notifications
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.menu_notifications);
        // Đổi màu nền của Badge
        badgeDrawable.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        badgeDrawable.setVisible(true); // Hiển thị badge
        badgeDrawable.setNumber(10); // Đặt số thông báo (thay đổi số này theo nhu cầu)
    }

    private void SetVisibleBadgeDrawableOrder() {
        // Thêm BadgeDrawable cho mục Notifications
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.menu_orders);
        // Đổi màu nền của Badge
        badgeDrawable.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        badgeDrawable.setVisible(true); // Hiển thị badge
        badgeDrawable.setNumber(2); // Đặt số thông báo (thay đổi số này theo nhu cầu)
    }
}
