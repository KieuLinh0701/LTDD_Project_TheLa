package com.TheLa.fragments.me;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.TheLa.Api.AddressApi;
import com.TheLa.Api.ApiClient;
import com.TheLa.Api.ApiResponse;
import com.TheLa.dto.AddressDto;
import com.TheLa.dto.UserDto;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeAddressFragment extends Fragment {

    private ImageView btnBack;
    private AppCompatButton btnSubmit;
    private EditText etCity, etDistric, etWard, etDetail;
    private AddressDto address;
    private UserDto user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_address, container, false);

        initialViews(view);
        initialData();
        addEvents();

        return view;

    }

    private void initialViews(View view) {
        btnBack = view.findViewById(R.id.btnBack);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        etCity = view.findViewById(R.id.etCity);
        etDistric = view.findViewById(R.id.etDistric);
        etWard = view.findViewById(R.id.etWard);
        etDetail = view.findViewById(R.id.etDetail);
    }

    private void initialData() {
        GetUser();
        setAddress();
    }

    private void addEvents() {
        btnBack.setOnClickListener(v -> btnBackClick());
        btnSubmit.setOnClickListener(v -> btnSubmitClick());
    }

    private void btnSubmitClick() {
        String city = etCity.getText().toString().trim();
        String district = etDistric.getText().toString().trim();
        String commune = etWard.getText().toString().trim();
        String detail = etDetail.getText().toString().trim();

        if (!isValidInput(city, district, commune, detail)) {
            return;
        }

        AddressDto dto;

        if (address == null) {
            dto = new AddressDto(city, district, commune, detail);
            CallApiNewAddress(dto);
        } else {
            dto = new AddressDto(address.getAddressId(), city, district, commune, detail);
            CallApiUpdateAddress(dto);
        }
    }

    private void CallApiUpdateAddress(AddressDto dto) {
        AddressApi api = ApiClient.getRetrofitInstance().create(AddressApi.class);

        Call<ApiResponse> call = api.updateAddress(dto);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("ChangeNewAddressFragment", "API phản hồi với mã: " + response.code());
                if (response.isSuccessful() && response.body() != null) {

                    address.setCity(dto.getCity());
                    address.setDistrict(dto.getDistrict());
                    address.setCommune(dto.getCommune());
                    address.setDetail(dto.getDetail());
                    saveUser(user);

                    Toast.makeText(getContext(), "Cập nhật địa chỉ thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                        if (errorResponse != null && errorResponse.getMessage() != null) {
                            Toast.makeText(getContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật địa chỉ thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Cập nhật địa chỉ thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("ChangeNewAddressFragment", "Lỗi khi gọi API: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CallApiNewAddress(AddressDto dto) {
        AddressApi api = ApiClient.getRetrofitInstance().create(AddressApi.class);

        Call<AddressDto> call = api.newAddress(dto, user.getUserId());

        call.enqueue(new Callback<AddressDto>() {
            @Override
            public void onResponse(Call<AddressDto> call, Response<AddressDto> response) {
                Log.d("ChangeNewAddressFragment", "API phản hồi với mã: " + response.code());
                if (response.isSuccessful() && response.body() != null) {

                    AddressDto dto = response.body();
                    user.setAddress(dto);
                    saveUser(user);

                    Toast.makeText(getContext(), "Thêm địa chỉ thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                        if (errorResponse != null && errorResponse.getMessage() != null) {
                            Toast.makeText(getContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Thêm địa chỉ thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Thêm địa chỉ thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddressDto> call, Throwable t) {
                Log.e("ChangeNewAddressFragment", "Lỗi khi gọi API: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUser(UserDto user) {
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(getContext());
        preferenceManager.saveUser(user);
    }

    private void GetUser() {
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(getContext());
        user = preferenceManager.getUser();
    }

    private void setAddress() {
        address = user.getAddress();
        if (address != null) {
            Log.e("address", address.getAddressId() + "");
            etCity.setText(address.getCity());
            etDistric.setText(address.getDistrict());
            etWard.setText(address.getCommune());
            etDetail.setText(address.getDetail());
        } else {
            Log.e("address", "Không có");
        }
    }

    private void btnBackClick() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private boolean isValidInput(String city, String district, String commune, String detail) {
        boolean isValid = true;

        // Kiểm tra tỉnh/thành phố
        if (city.isEmpty()) {
            etCity.setError("Vui lòng chọn tỉnh/thành phố nơi giao hàng!");
            etCity.requestFocus();
            isValid = false;
        }

        // Kiểm tra huyện/quận
        if (district.isEmpty()) {
            etDistric.setError("Vui lòng chọn quận/huyện nơi giao hàng!");
            etDistric.requestFocus();
            isValid = false;
        }

        // Kiểm tra xã/phường
        if (commune.isEmpty()) {
            etWard.setError("Vui lòng chọn phường/xã nơi giao hàng!");
            etWard.requestFocus();
            isValid = false;
        }

        // Kiểm tra địa chỉ chi tiết
        if (detail.isEmpty()) {
            etDetail.setError("Vui lòng nhập địa chỉ chi tiết (ví dụ: số nhà, tên đường)!");
            etDetail.requestFocus();
            isValid = false;
        }

        return isValid;
    }
}