package com.TheLa.fragments.me;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.TheLa.utils.AppUtils;
import com.example.TheLa.R;

public class ChangeEmailFragment extends Fragment {

    private EditText etEmail;
    private ImageView ivBack;
    private AppCompatButton btnNext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_change_email, container, false);

        initializeViews(view);

        addEvents();

        return view;
    }

    private void initializeViews(View view) {
        etEmail = view.findViewById(R.id.etEmail);
        ivBack = view.findViewById(R.id.ivBack);
        btnNext = view.findViewById(R.id.btnNext);
    }

    private void addEvents() {
        ivBack.setOnClickListener(
                v -> ivBackClick()
        );
    }

    private void ivBackClick() {
        AppUtils.setBottomNavigationVisibility(this, true);
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}