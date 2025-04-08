package com.TheLa.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.TheLa.activities.LoginActivity;
import com.TheLa.activities.RegisterActivity;
import com.TheLa.dto.UserDto;
import com.TheLa.fragments.me.AddressFragment;
import com.TheLa.fragments.me.ChangeEmailFragment;
import com.TheLa.fragments.me.ChangePasswordFragment;
import com.TheLa.fragments.me.PaymentHistoryFragment;
import com.TheLa.fragments.me.PaymentMethodFragment;
import com.TheLa.fragments.me.ProfileFragment;
import com.TheLa.utils.AppUtils;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;

public class MeFragment extends Fragment {
    private TextView tvUserName, tvEmail;
    private ImageView editButton;
    private FrameLayout cart;
    private AppCompatButton btnLogin, btnSignUp;
    private LinearLayout authButtons, logout, profile, myAddress, email, password, paymentMethod, paymentHistory;
    private UserDto user;
    private SharedPreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_me, container, false);

        preferenceManager = new SharedPreferenceManager(requireContext());

        initializeViews(view);
        initializeData();

        addEvents();

        return view;
    }

    private void initializeViews(View view) {
        tvUserName = view.findViewById(R.id.tvUserName);
        tvEmail = view.findViewById(R.id.tvEmail);
        editButton = view.findViewById(R.id.editButton);
        authButtons = view.findViewById(R.id.authButtons);
        logout = view.findViewById(R.id.logout);
        profile = view.findViewById(R.id.profile);
        myAddress = view.findViewById(R.id.myAddress);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        cart = view.findViewById(R.id.cart);
        paymentMethod = view.findViewById(R.id.paymentMethod);
        paymentHistory = view.findViewById(R.id.paymentHistory);
    }

    private void addEvents() {
        logout.setOnClickListener(v -> logoutClick());
        profile.setOnClickListener(v -> profileClick());
        myAddress.setOnClickListener(v -> myAddressClick());
        email.setOnClickListener(v -> emailClick());
        password.setOnClickListener(v -> passwordClick());
        btnLogin.setOnClickListener(v -> btnLoginClick());
        btnSignUp.setOnClickListener(v -> btnSignUpClick());
        paymentMethod.setOnClickListener(v -> paymentMethodClick());
        paymentHistory.setOnClickListener(v -> paymentHistoryClick());
        cart.setOnClickListener(v -> cartClick());
        editButton.setOnClickListener(v -> profileClick());
    }

    private void initializeData() {
        if (preferenceManager.isLoggedIn()) {
            getUser();
            setName();
            setEmail();
            // Ẩn linearlayout chứa button Login, SignUp
            authButtons.setVisibility(View.GONE);

            // Hiển thị sửa profile và tên người dùng
            editButton.setVisibility(View.VISIBLE);
            tvUserName.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
        } else {
            // Hiển thị linearlayout chứa button Login, SignUp
            authButtons.setVisibility(View.VISIBLE);

            // Ẩn sửa profile và tên người dùng
            editButton.setVisibility(View.GONE);
            tvUserName.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
        }
    }

    private void cartClick() {
        AppUtils.setBottomNavigationVisibility(this, false);
        AppUtils.checkLogin(R.id.fragment_me, getContext(), this, new CartFragment());
    }

    private void paymentHistoryClick() {
        AppUtils.setBottomNavigationVisibility(this, false);
        AppUtils.checkLogin(R.id.fragment_me, getContext(), this, new PaymentHistoryFragment());
    }

    private void paymentMethodClick() {
        AppUtils.setBottomNavigationVisibility(this, false);
        AppUtils.checkLogin(R.id.fragment_me, getContext(), this, new PaymentMethodFragment());
    }

    private void btnLoginClick() {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void btnSignUpClick() {
        Intent intent = new Intent(requireContext(), RegisterActivity.class);
        startActivity(intent);
    }

    private void passwordClick() {
        if (preferenceManager.isLoggedIn()) {
            AppUtils.setBottomNavigationVisibility(this, false);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_me, new ChangePasswordFragment())
                    .addToBackStack("ChangePasswordFragment")
                    .commit();
        } else {
            AppUtils.switchToLoginActivity(getContext());
        }
    }

    private void emailClick() {
        AppUtils.setBottomNavigationVisibility(this, false);
        AppUtils.checkLogin(R.id.fragment_me, getContext(), this, new ChangeEmailFragment());
    }

    private void myAddressClick() {
        AppUtils.setBottomNavigationVisibility(this, false);
        AppUtils.checkLogin(R.id.fragment_me, getContext(), this, new AddressFragment());
    }

    private void profileClick() {
        AppUtils.setBottomNavigationVisibility(this, false);
        AppUtils.checkLogin(R.id.fragment_me, getContext(), this, new ProfileFragment());
    }

    private void logoutClick() {
        // Xóa thông tin người dùng khi đăng xuất
        preferenceManager.clearUser();

        // Sau khi đăng xuất, reload lại fragment hiện tại
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.detach(MeFragment.this);  // Tách fragment hiện tại ra
        transaction.attach(MeFragment.this);  // Gắn lại fragment hiện tại
        transaction.commit();

        // Sau khi đăng xuất, hiển thị lại thông tin và các nút Login, Sign Up
        initializeData();
    }


    //Lấy thông tin user
    private void getUser() {
        user = preferenceManager.getUser();
    }

    private void setName() {
        tvUserName.setText(user.getName());
    }

    private void setEmail() {
        String email = user.getEmail();

        // Find the index of '@' in the email
        int atIndex = email.indexOf('@');

        // If '@' is found and the email is valid
        if (atIndex > 1) {
            // Keep the first character and the last character before '@'
            String hint = email.charAt(0) +
                    email.substring(1, atIndex - 1).replaceAll(".", "*") +
                    email.charAt(atIndex - 1) +
                    email.substring(atIndex);
            tvEmail.setText(hint);
        } else {
            // Fallback for invalid email
            tvEmail.setText(email);
        }
    }
}