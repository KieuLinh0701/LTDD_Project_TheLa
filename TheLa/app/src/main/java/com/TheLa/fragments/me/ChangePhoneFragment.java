package com.TheLa.fragments.me;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.TheLa.Api.ApiClient;
import com.TheLa.Api.ApiResponse;
import com.TheLa.Api.UserApi;
import com.TheLa.dto.UserDto;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePhoneFragment extends Fragment {

    private ImageView ivBack;
    private AppCompatButton btnNext;
    private EditText etPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_phone, container, false);

        initialViews(view);

        addEvents();

        return view;
    }

    private void initialViews(View view) {
        ivBack = view.findViewById(R.id.ivBack);
        btnNext = view.findViewById(R.id.btnNext);
        etPhone = view.findViewById(R.id.etPhone);
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

        String phone = etPhone.getText().toString().trim();

        UserDto user = getUser();

        if (!isValidInput(phone, user.getPhone())) {
            return;
        }

        user.setPhone(phone);

        Call<ApiResponse> call = userApi.save(user);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("ChangeFragment", "API phản hồi với mã: " + response.code());
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
                    Log.d("ChangePhoneFragment", "Thay đổi thất bại!" + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("ChangePhoneFragment", "Lỗi khi gọi API: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidInput(String phone, String oldPhone) {
        if (phone.isEmpty()) {
            etPhone.setError("Vui lòng nhập số điện thoại!");
            etPhone.requestFocus();
            return false;
        } else if (!phone.matches("^[0-9]{10}$")) { // Kiểm tra số điện thoại chỉ chứa đúng 10 chữ số
            etPhone.setError("Số điện thoại không hợp lệ. Vui lòng nhập đúng 10 chữ số!");
            etPhone.requestFocus();
            return false;
        }else if (phone.equals(oldPhone)) {
            Toast.makeText(getContext(), "Số điện thoại mới không được trùng với số điện thoại hiện tại. Vui lòng chọn một số khác!", Toast.LENGTH_SHORT).show();
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