package com.TheLa.fragments.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TheLa.Api.ApiClient;
import com.TheLa.Api.PromotionApi;
import com.TheLa.adapters.PromotionAdapter;
import com.TheLa.dto.PromotionDto;
import com.example.TheLa.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromotionFragment extends Fragment {

    private RecyclerView rvPromotion;
    private PromotionAdapter promotionAdapter;
    private List<PromotionDto> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion, container, false);

        initializeViews(view);
        setUpPromotionRecyclerView();
        getPromotion();
        setupButtonBack(view);

        return view;
    }

    private void getPromotion() {
        PromotionApi api = ApiClient.getRetrofitInstance().create(PromotionApi.class);
        Call<List<PromotionDto>> call = api.getAllActiveAndNotDeletedPromotions();

        call.enqueue(new Callback<List<PromotionDto>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<PromotionDto>> call, Response<List<PromotionDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    list.clear();
                    list.addAll(response.body());

                    // Kiểm tra nếu Fragment đã gắn vào Activity trước khi cập nhật UI
                    promotionAdapter.notifyDataSetChanged();
                } else {
                    Log.e("PromotionFragment", "Lỗi: Không có dữ liệu hợp lệ từ API");
                }
            }

            @Override
            public void onFailure(Call<List<PromotionDto>> call, Throwable t) {
                Log.e("PromotionFragment", "Lỗi khi gọi API: " + t.getMessage());
            }
        });
    }

    private void setUpPromotionRecyclerView() {
        rvPromotion.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo adapter với danh sách rỗng
        promotionAdapter = new PromotionAdapter(list, getContext(), position -> {
            getPromotion();
            PromotionDto clicked = list.get(position);

            PromotionDetailFragment fragment = new PromotionDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("promotion", (Serializable) clicked);
            fragment.setArguments(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_bottom,
                            android.R.anim.fade_out,
                            android.R.anim.fade_out,
                            R.anim.slide_out_bottom
                    )
                    .replace(R.id.fragment_promotion, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        rvPromotion.setAdapter(promotionAdapter);
    }

    private void setupButtonBack(View view) {
        ImageView btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void initializeViews(View view) {
        rvPromotion = view.findViewById(R.id.promotionRecyclerView);
    }
}