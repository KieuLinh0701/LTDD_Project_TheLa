package com.TheLa.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.TheLa.fragments.HomeFragment;
import com.TheLa.fragments.MeFragment;
import com.TheLa.fragments.OrderFragment;
import com.TheLa.fragments.ShopFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behaviorResumeOnlyCurrentFragment) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new ShopFragment();
            case 2:
                return new OrderFragment();
            case 3:
                return new MeFragment();
            default:
                return new HomeFragment();
        }
    }
}
