package com.TheLa.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.TheLa.Api.ApiClient;
import com.TheLa.Api.ReviewApi;
import com.TheLa.adapters.ReviewAdapter;
import com.TheLa.dto.ReviewDto;
import com.example.TheLa.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewProductFragment extends Fragment {
    private Long productId;
    private ImageView btnBack;
    private RecyclerView rvReview;
    private ReviewAdapter adapter;
    List<ReviewDto> listReview = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_review_product, container, false);

        retrieveProductFromBundle();

        initializeViews(view);

        getReview();

        setupEventHandlers();
        return view;
    }

    private void retrieveProductFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            productId = (Long) bundle.getSerializable("productId");
        }
    }

    private void initializeViews(View view) {
        btnBack = view.findViewById(R.id.btnBack);
        rvReview = view.findViewById(R.id.reviewRecyclerView);
    }

    private void getReview() {
        ReviewApi api = ApiClient.getRetrofitInstance().create(ReviewApi.class);
        Call<List<ReviewDto>> call = api.getReviewsByProductId(productId);

        call.enqueue(new Callback<List<ReviewDto>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<ReviewDto>> call, Response<List<ReviewDto>> response) {
                listReview.clear();
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    listReview = response.body();

                    // Gọi hàm SetUpReviewRecyclerView() sau khi nhận được kết quả API
                    SetUpReviewRecyclerView();

                    // Thông báo cho adapter cập nhật dữ liệu
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ReviewDto>> call, Throwable t) {
                Log.e("ProductDetailFragment", "Lỗi khi gọi API: " + t.getMessage());
            }
        });
    }

    private void setupEventHandlers() {
        btnBack.setOnClickListener(v -> handleBackButtonClick());
    }

    // Xử lý sự kiện khi nút đóng được nhấn
    private void handleBackButtonClick() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void SetUpReviewRecyclerView() {
        adapter = new ReviewAdapter(listReview, getContext());
        rvReview.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReview.setAdapter(adapter);
    }
}