package com.TheLa.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.TheLa.fragments.home.ProductDetail.InformationProductDetailFragment;
import com.TheLa.fragments.home.ProductDetail.ReviewProductDetailFragment;

public class ProductDetailViewPagerAdapter extends FragmentStateAdapter {
    private Bundle product;

    public ProductDetailViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Bundle product) {
        super(fragmentManager, lifecycle);
        this.product = product;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new InformationProductDetailFragment();
                break;
            case 1:
                fragment = new ReviewProductDetailFragment();
                break;
            default:
                fragment = new InformationProductDetailFragment();
                break;
        }
        if (product != null) {
            fragment.setArguments(product);
        }
        return fragment;
    }


    @Override
    public int getItemCount() {
        return 2; // Số lượng tab
    }
}
