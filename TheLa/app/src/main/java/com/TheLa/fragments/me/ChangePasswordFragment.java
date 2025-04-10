package com.TheLa.fragments.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.TheLa.Api.ApiClient;
import com.TheLa.Api.ApiResponse;
import com.TheLa.Api.UserApi;
import com.TheLa.activities.VerificationAccountActivity;
import com.TheLa.dto.UserDto;
import com.TheLa.utils.AppUtils;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {
    private ImageView btnBack;
    private UserDto user;
    private Call<ApiResponse> call;
    private boolean isFragmentActive = false;

    @Override
    public void onStart() {
        super.onStart();
        isFragmentActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isFragmentActive = false;

        if (call != null && ! call.isCanceled()) {
            call.cancel();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data != null && "backToMeFragment".equals(data.getStringExtra("action"))) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                // Xóa ChangePasswordFragment khỏi stack
                fragmentManager.popBackStack("ChangePasswordFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                AppUtils.setBottomNavigationVisibility(ChangePasswordFragment.this, true);
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        initializeViews(view);

        getUser();

        callAPISendEmail();

        setupButtonBack();

        return view;
    }

    private void initializeViews(View view) {
        btnBack = view.findViewById(R.id.btnBack);
    }

    private void setupButtonBack() {
        btnBack.setOnClickListener(v -> {
            if (isAdded()) {
                requireActivity().getSupportFragmentManager().popBackStack();
                AppUtils.setBottomNavigationVisibility(ChangePasswordFragment.this, true);
            }
        });
    }

    private void getUser() {
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(getContext());
        user = preferenceManager.getUser();

    }

    private void callAPISendEmail() {
        if (call != null && !call.isCanceled() && !call.isExecuted()) {
            return; // Đã có một cuộc gọi đang hoạt động
        }

        UserApi userApi = ApiClient.getRetrofitInstance().create(UserApi.class);

        call = userApi.sendEmailVerifyAccount(user.getEmail(), "ChangePassword");

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (!isFragmentActive) return;

                Log.d("VerifyAccountActivity", "API phản hồi với mã: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Vui lòng kiểm tra email của bạn và nhập mã OTP để hoàn tất xác thực!", Toast.LENGTH_SHORT).show();
                    requireView().postDelayed(() -> {
                        if (isFragmentActive) {
                            Intent intent = new Intent(requireContext(), VerificationAccountActivity.class);
                            intent.putExtra("email", user.getEmail());
                            intent.putExtra("feature", "ChangePassword");
                            startActivityForResult(intent, 100);
                        }
                    }, 2000);
                } else {
                    try {
                        ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                        if (errorResponse != null && errorResponse.getMessage() != null) {
                            Toast.makeText(getContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Gửi email xác thực tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Gửi email xác thực tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("MeFragment", "Gửi email xác thực tài khoản thất bại: " + response.message());
                    requireActivity().getSupportFragmentManager().popBackStack();
                    AppUtils.setBottomNavigationVisibility(ChangePasswordFragment.this, true);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (!isFragmentActive) return;

                Log.e("MeFragment", "Lỗi khi gọi API: " + t.getMessage());
                requireActivity().getSupportFragmentManager().popBackStack();
                AppUtils.setBottomNavigationVisibility(ChangePasswordFragment.this, true);
            }
        });
    }

}