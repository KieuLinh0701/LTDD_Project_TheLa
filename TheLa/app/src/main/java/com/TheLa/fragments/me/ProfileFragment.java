package com.TheLa.fragments.me;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.TheLa.dto.UserDto;
import com.TheLa.utils.AppUtils;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;

public class ProfileFragment extends Fragment {
    private LinearLayout name, image;
    private ImageView ivBack;
    private UserDto user;
    private TextView tvEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        intialViews(view);
        getUser();
        addEvents();

        return view;
    }

    private void intialViews(View view) {
        name = view.findViewById(R.id.name);
        image = view.findViewById(R.id.image);
        ivBack = view.findViewById(R.id.ivBack);
    }

    private void addEvents() {
        ivBack.setOnClickListener(v -> ivBackClick());
        name.setOnClickListener(v -> nameClick());
    }

    private void nameClick() {
       AppUtils.switchToFragment(this, new ChangeNameFragment(), R.id.fragment_profile);
    }

    private void ivBackClick() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void getUser() {
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(getContext());
        user = preferenceManager.getUser();
    }
}