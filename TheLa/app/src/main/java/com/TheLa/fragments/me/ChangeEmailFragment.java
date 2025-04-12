package com.TheLa.fragments.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.TheLa.Api.ApiClient;
import com.TheLa.Api.ApiResponse;
import com.TheLa.Api.UserApi;
import com.TheLa.activities.ForgotPasswordEmailActivity;
import com.TheLa.activities.VerificationAccountActivity;
import com.TheLa.dto.UserDto;
import com.TheLa.fragments.MeFragment;
import com.TheLa.utils.AppUtils;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeEmailFragment extends Fragment {

    private EditText etEmail;
    private ImageView ivBack;
    private AppCompatButton btnNext;
    private UserDto user;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data != null && "backToMeFragment".equals(data.getStringExtra("action"))) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                // Xóa ChangePasswordFragment khỏi stack (nếu có)
                fragmentManager.popBackStack("ChangeEmailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                // Quay lại Fragment trước đó
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_change_email, container, false);

        initializeViews(view);

        getUser();

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

        btnNext.setOnClickListener(
                v -> btnNextClick()
        );
    }

    private void getUser() {
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(getContext());
        user = preferenceManager.getUser();

    }

    private void btnNextClick() {
        String newEmail = etEmail.getText().toString().trim();

        // Validate input
        if (!isValidInput(newEmail)) {
            return;
        }

        callAPISendEmail(newEmail);

    }

    private void ivBackClick() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void callAPISendEmail(String newEmail) {
        UserApi userApi = ApiClient.getRetrofitInstance().create(UserApi.class);

        Call<ApiResponse> call = userApi.sendEmailResetEmail(user.getEmail(), newEmail);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    SwitchToVerificationAccount(newEmail);

                } else {
                    try {
                        ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                        if (errorResponse != null && errorResponse.getMessage() != null) {
                            Toast.makeText(getContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Xác thực tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Xác thực tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("ChangeEmailFragment", "Xác thực tài khoản thất bại: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("ChangeEmailFragment", "Lỗi khi gọi API: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SwitchToVerificationAccount(String newEmail) {
        Toast.makeText(getContext(), "Vui lòng kiểm tra email của bạn và nhập mã OTP để hoàn tất xác thực!", Toast.LENGTH_SHORT).show();
        requireView().postDelayed(() -> {
            Intent intent = new Intent(requireContext(), VerificationAccountActivity.class);
            intent.putExtra("email", newEmail);
            intent.putExtra("feature", "ChangeEmail");
            startActivityForResult(intent, 100);
        }, 2000);
    }

    private boolean isValidInput(String email) {
        if (email.isEmpty()) {
            etEmail.setError("Vui lòng nhập email!");
            etEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ!");
            etEmail.requestFocus();
            return false;
        } else if (email.equals(user.getEmail())) {
            Toast.makeText(getContext(), "Email mới không được trùng với email hiện tại. Vui lòng nhập một email khác!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}