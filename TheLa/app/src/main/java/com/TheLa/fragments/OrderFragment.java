package com.TheLa.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.TheLa.adapters.OrderViewPagerAdapter;
import com.TheLa.fragments.order.SearchOrderFragment;
import com.TheLa.fragments.widget.CustomViewPager;
import com.example.TheLa.R;
import com.google.android.material.tabs.TabLayout;

public class OrderFragment extends Fragment {

    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private View mView;

    private ImageView btnSearch;

    public OrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_order, container, false);

        tabLayout = mView.findViewById(R.id.tabLayout);
        viewPager = mView.findViewById(R.id.orderViewPager);
        btnSearch = mView.findViewById(R.id.btnSearch);

        OrderViewPagerAdapter adapter = new OrderViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.setPagingEnabled(true);
        tabLayout.setupWithViewPager(viewPager);

        setBtnSearchClick();

        return mView;
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
                setBottomNavigationVisibility(false);
            }
        });
    }

    private void setBottomNavigationVisibility(boolean isVisible) {
        View bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }
}