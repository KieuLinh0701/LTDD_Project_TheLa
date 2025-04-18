package com.TheLa.fragments.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

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
import com.TheLa.activities.VerificationAccountActivity;
import com.TheLa.dto.UserDto;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeNameFragment extends Fragment {

    private ImageView ivBack;
    private AppCompatButton btnNext;
    private EditText etName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_name, container, false);

        initialViews(view);

        addEvents();

        return view;
    }

    private void initialViews(View view) {
        ivBack = view.findViewById(R.id.ivBack);
        btnNext = view.findViewById(R.id.btnNext);
        etName = view.findViewById(R.id.etName);
    }

    private void addEvents() {
        btnNext.setOnClickListener(v -> btnNextClick());
        ivBack.setOnClickListener(v -> ivBackClick());
    }

    private void btnNextClick() {
        CallAPISaveUser();
    }

    private void CallAPISaveUser() {
        UserApi userApi = ApiClient.getRetrofitInstance().create(UserApi.class);

        String name = etName.getText().toString().trim();

        UserDto user = getUser();

        if (!isValidInput(name, user.getName())) {
            return;
        }

        user.setName(name);

        Call<ApiResponse> call = userApi.save(user);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("ChangeNameFragment", "API phản hồi với mã: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    saveUser(user);

                    Toast.makeText(getContext(), "Thay đổi thành công!", Toast.LENGTH_SHORT).show();
                    new android.os.Handler().postDelayed(() -> {
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }, 2000);

                } else {
                    // Xử lý lỗi từ API (bao gồm BadRequest)
                    try {
                        ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                        if (errorResponse != null && errorResponse.getMessage() != null) {
                            Toast.makeText(getContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Thay đổi thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Thay đổi thất bại!", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("ChangeNameFragment", "Thay đổi thất bại!" + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("ChangeNameFragment", "Lỗi khi gọi API: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidInput(String name, String oldName) {
        if (name.isEmpty()) {
            etName.setError("Vui lòng nhập tên!");
            etName.requestFocus();
            return false;
        } else if (name.equals(oldName)) {
            Toast.makeText(getContext(), "Tên mới không được trùng với tên hiện tại. Vui lòng chọn một tên khác!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveUser(UserDto user) {
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(getContext());
        preferenceManager.saveUser(user);
    }

    private UserDto getUser() {
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(getContext());
        UserDto user = preferenceManager.getUser();

        return user;
    }

    private void ivBackClick() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}