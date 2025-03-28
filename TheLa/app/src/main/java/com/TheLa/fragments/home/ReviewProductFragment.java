package com.TheLa.fragments.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.TheLa.adapters.ReviewAdapter;
import com.TheLa.models.ReviewModel;
import com.TheLa.services.implement.ReviewService;
import com.example.TheLa.R;

import java.util.List;

public class ReviewProductFragment extends Fragment {
    private Long productId;
    private ImageView btnBack;
    private RecyclerView rvReview;
    private ReviewService reviewService = new ReviewService();
    private ReviewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_review_product, container, false);

        retrieveProductFromBundle();
        initializeViews(view);
        SetUpReviewRecyclerView();
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

    private void setupEventHandlers() {
        btnBack.setOnClickListener(v -> handleBackButtonClick());
    }

    // Xử lý sự kiện khi nút đóng được nhấn
    private void handleBackButtonClick() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void SetUpReviewRecyclerView() {
        List<ReviewModel> list = reviewService.findReviewByProductId(productId);

        adapter = new ReviewAdapter(list, getContext());
        rvReview.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReview.setAdapter(adapter);
    }
}