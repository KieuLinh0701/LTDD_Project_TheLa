package com.TheLa.fragments.order;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.TheLa.fragments.HomeFragment;
import com.TheLa.fragments.MeFragment;
import com.TheLa.fragments.OrderFragment;
import com.TheLa.fragments.ShopFragment;

public class OrderViewPagerAdapter extends FragmentStatePagerAdapter {
    public OrderViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior); // Sử dụng behavior ở đây
    }

    @Override
    public int getCount() {
        return 3; // Số lượng tab
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProcessingOrderFragment();
            case 1:
                return new CompletedOrderFragment();
            case 2:
                return new CancelledOrderFragment();
            default:
                return new ProcessingOrderFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Processing";
            case 1:
                return "Completed";
            case 2:
                return "Cancelled";
            default:
                return "Processing";
        }
    }
}

