package com.TheLa.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.TheLa.activities.LoginActivity;
import com.TheLa.activities.MainActivity;
import com.TheLa.activities.RegisterActivity;
import com.TheLa.dto.UserDto;
import com.TheLa.fragments.me.ChangeAddressFragment;
import com.TheLa.fragments.me.ChangeEmailFragment;
import com.TheLa.fragments.me.ChangePasswordFragment;
import com.TheLa.fragments.me.PaymentHistoryFragment;
import com.TheLa.fragments.me.PaymentMethodFragment;
import com.TheLa.fragments.me.ProfileFragment;
import com.TheLa.utils.AppUtils;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;

import java.io.InputStream;

public class MeFragment extends Fragment {
    private TextView tvUserName, tvEmail;
    private ImageView editButton, image;
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
        image = view.findViewById(R.id.image);
    }

    private void addEvents() {
        logout.setOnClickListener(v -> logoutClick());
        profile.setOnClickListener(v -> profileClick());
        myAddress.setOnClickListener(v -> addressClick());
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
            setImage();

            // Ẩn linearlayout chứa button Login, SignUp
            authButtons.setVisibility(View.GONE);

            // Hiển thị sửa profile và tên người dùng
            editButton.setVisibility(View.VISIBLE);
            tvUserName.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.VISIBLE);
        } else {
            // Hiển thị linearlayout chứa button Login, SignUp
            authButtons.setVisibility(View.VISIBLE);

            // Ẩn sửa profile và tên người dùng
            editButton.setVisibility(View.GONE);
            tvUserName.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
            tvEmail.setVisibility(View.GONE);
        }
    }

    private void cartClick() {

        if (handleLoginCheck()) {
            AppUtils.switchToFragment(this, new CartFragment(), R.id.fragment_me);
        }

        BackStackChangedListener();
    }

    private void paymentHistoryClick() {
        if (handleLoginCheck()) {
            AppUtils.switchToFragment(this, new PaymentHistoryFragment(), R.id.fragment_me);
        }

        BackStackChangedListener();
    }

    private void paymentMethodClick() {
        if (handleLoginCheck()) {
            AppUtils.switchToFragment(this, new PaymentMethodFragment(), R.id.fragment_me);
        }

        BackStackChangedListener();
    }

    private void btnLoginClick() {
        SwitchToLoginActivity();
    }

    /// Cái này phải sửa để có thể quay về đúng trang me theo Login nha
    private void btnSignUpClick() {
        Intent intent = new Intent(requireContext(), RegisterActivity.class);
        startActivity(intent);
    }

    private void passwordClick() {
        if (handleLoginCheck()) {
            SwicthToFragment("ChangePasswordFragment", new ChangePasswordFragment());
        }
        BackStackChangedListener();
    }

    private void emailClick() {
        if (preferenceManager.isLoggedIn()) {
            SwicthToFragment("ChangeEmailFragment", new ChangeEmailFragment());
        } else {
            SwitchToLoginActivity();
        }

        BackStackChangedListener();
    }

    private void addressClick() {
        if (handleLoginCheck()) {
            AppUtils.switchToFragment(this, new ChangeAddressFragment(), R.id.fragment_me);
        }

        BackStackChangedListener();
    }

    private void profileClick() {
        if (handleLoginCheck()) {
            AppUtils.switchToFragment(this, new ProfileFragment(), R.id.fragment_me);
        }

        BackStackChangedListener();
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

    //Set ảnh cho user
    private void setImage() {
        String imageName = user.getImage(); // Lấy tên ảnh từ user

        if (imageName != null && !imageName.isEmpty()) {
            // Tìm tài nguyên trong thư mục res/raw
            int resourceId = getResources().getIdentifier(imageName, "raw", requireContext().getPackageName());
            if (resourceId != 0) {
                // Tải ảnh từ res/raw
                InputStream inputStream = getResources().openRawResource(resourceId);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(bitmap);
            } else {
                // Đặt ảnh mặc định nếu không tìm thấy tài nguyên
                image.setImageResource(R.drawable.avatar_default);
            }
        } else {
            // Đặt ảnh mặc định nếu tên ảnh không hợp lệ
            image.setImageResource(R.drawable.avatar_default);
        }
    }


    //Lấy thông tin user
    private void getUser() {
        user = preferenceManager.getUser();
    }

    private void setName() {
        tvUserName.setText(user.getName());
    }

    private void setEmail() {
        tvEmail.setText(AppUtils.setEmail(user.getEmail()));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cập nhật lại dữ liệu khi quay lại fragment
        initializeData();
    }

    private boolean handleLoginCheck() {
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(getContext());
        if (!preferenceManager.isLoggedIn()) {
            SwitchToActivityLoginDelay();
            return false;
        }
        return true;
    }

    private void SwitchToActivityLoginDelay() {
        Toast.makeText(getContext(), "Đăng nhập ngay để tiếp tục trải nghiệm thú vị nào!", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(this::SwitchToLoginActivity, 2000); // Trì hoãn 2000ms
    }

    private void SwitchToLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.putExtra("previousPage", "me"); // Ghi lại trang trước đó
        startActivity(intent);
    }

    private void SwicthToFragment(String name, Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_me, fragment)
                .addToBackStack(name)
                .commit();
    }

    private void BackStackChangedListener() {
        // Ẩn BottomNavigationView
        ((MainActivity) getActivity()).setBottomNavigationVisibility(false);

        // Lắng nghe sự kiện quay lại để hiển thị lại BottomNavigationView
        getParentFragmentManager().addOnBackStackChangedListener(() -> {

            if (isVisible()) { // Kiểm tra nếu `MeFragment` đang hiển thị
                initializeData(); // Làm mới dữ liệu
            }

            if (getParentFragmentManager().getBackStackEntryCount() == 0) {
                ((MainActivity) requireActivity()).setBottomNavigationVisibility(true);
            }
        });
    }
}