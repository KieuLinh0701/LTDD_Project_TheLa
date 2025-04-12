package com.TheLa.fragments.me;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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

import java.io.InputStream;

public class ProfileFragment extends Fragment {
    private LinearLayout name, image, phone;
    private TextView tvPhone;
    private ImageView ivBack;
    private UserDto user;
    private ImageView ivImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initialViews(view);
        initialData();
        addEvents();

        return view;
    }

    private void initialViews(View view) {
        name = view.findViewById(R.id.name);
        image = view.findViewById(R.id.image);
        phone = view.findViewById(R.id.phone);
        ivBack = view.findViewById(R.id.ivBack);
        ivImage = view.findViewById(R.id.ivImage);
        tvPhone = view.findViewById(R.id.tvPhone);
    }

    private void initialData() {
        getUser();
        setImage();
        setTextViewPhone();
    }

    private void addEvents() {
        ivBack.setOnClickListener(v -> ivBackClick());
        name.setOnClickListener(v -> nameClick());
        image.setOnClickListener(v -> imageClick());
        phone.setOnClickListener(v -> phoneClick());
    }

    private void phoneClick() {
        AppUtils.switchToFragment(this, new ChangePhoneFragment(), R.id.fragment_profile);
    }

    private void imageClick() {
        AppUtils.switchToFragment(this, new ChangeImageFragment(), R.id.fragment_profile);
    }

    private void nameClick() {
       AppUtils.switchToFragment(this, new ChangeNameFragment(), R.id.fragment_profile);
    }

    private void ivBackClick() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @SuppressLint("SetTextI18n")
    private void setTextViewPhone() {
        String phone = user.getPhone();

        if (phone == null) {
            tvPhone.setText("null");
            return;
        }

        String lastTwoDigits = phone.substring(phone.length() - 2);
        String hiddenPart = phone.substring(0, phone.length() - 2).replaceAll("\\d", "*");
        tvPhone.setText(hiddenPart + lastTwoDigits);
    }

    private void getUser() {
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(getContext());
        user = preferenceManager.getUser();
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
                ivImage.setImageBitmap(bitmap);
            } else {
                // Đặt ảnh mặc định nếu không tìm thấy tài nguyên
                ivImage.setImageResource(R.drawable.avatar_default);
            }
        } else {
            // Đặt ảnh mặc định nếu tên ảnh không hợp lệ
            ivImage.setImageResource(R.drawable.avatar_default);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initialData();
    }
}