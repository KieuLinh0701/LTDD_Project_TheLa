package com.TheLa.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.TheLa.fragments.MeFragment;
import com.TheLa.fragments.OrderFragment;
import com.TheLa.fragments.HomeFragment;
import com.TheLa.fragments.StoreFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new StoreFragment();
            case 2:
                return new OrderFragment();
            case 3:
                return new MeFragment();
            default:
                return new HomeFragment();
        }
    }
}
