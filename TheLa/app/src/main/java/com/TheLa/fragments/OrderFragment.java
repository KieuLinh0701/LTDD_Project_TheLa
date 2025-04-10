package com.TheLa.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.TheLa.activities.LoginActivity;
import com.TheLa.activities.MainActivity;
import com.TheLa.adapters.OrderViewPagerAdapter;
import com.TheLa.fragments.order.SearchOrderFragment;
import com.TheLa.fragments.widget.CustomViewPager;
import com.TheLa.utils.AppUtils;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;
import com.google.android.material.tabs.TabLayout;

public class OrderFragment extends Fragment {

    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private View mView;
    private ImageView btnSearch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_order, container, false);

        tabLayout = mView.findViewById(R.id.tabLayout);
        viewPager = mView.findViewById(R.id.orderViewPager);
        btnSearch = mView.findViewById(R.id.btnSearch);

        initTabsAndViewPager();
        setBtnSearchClick();

        return mView;
    }

    private void initTabsAndViewPager() {
        OrderViewPagerAdapter adapter = new OrderViewPagerAdapter(
                getChildFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        );
        viewPager.setAdapter(adapter);
        viewPager.setPagingEnabled(true);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setBtnSearchClick() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến SearchOrderFragment khi nhấn vào biểu tượng tìm kiếm
                SearchOrderFragment searchOrderFragment = new SearchOrderFragment();

                // Sử dụng getChildFragmentManager() vì đây là fragment con
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                // Thay R.id.fragment_container bằng ID của container chứa fragment bạn muốn thay thế
                transaction.replace(R.id.fragment_order, searchOrderFragment);
                transaction.addToBackStack(null); // Để cho phép quay lại fragment cũ
                transaction.commit();

                // Ẩn BottomNavigationView
                ((MainActivity) getActivity()).setBottomNavigationVisibility(false);

                // Lắng nghe sự kiện quay lại để hiển thị lại BottomNavigationView
                getParentFragmentManager().addOnBackStackChangedListener(() -> {
                    if (getParentFragmentManager().getBackStackEntryCount() == 0) {
                        ((MainActivity) requireActivity()).setBottomNavigationVisibility(true);
                    }
                });

            }
        });
    }
}