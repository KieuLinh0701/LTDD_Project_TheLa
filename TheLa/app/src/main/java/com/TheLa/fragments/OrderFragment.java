package com.TheLa.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TheLa.fragments.order.OrderViewPagerAdapter;
import com.TheLa.fragments.widget.CustomViewPager;
import com.example.TheLa.R;
import com.google.android.material.tabs.TabLayout;

public class OrderFragment extends Fragment {

    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private View mView;


    public OrderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_order, container, false);

        tabLayout = mView.findViewById(R.id.tabLayout);
        viewPager = mView.findViewById(R.id.orderViewPager);

        OrderViewPagerAdapter adapter = new OrderViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.setPagingEnabled(false);
        tabLayout.setupWithViewPager(viewPager);


        return mView;
    }
}