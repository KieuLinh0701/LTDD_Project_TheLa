package com.TheLa.fragments.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.TheLa.adapters.CategoryAdapter;
import com.TheLa.adapters.ProductAdapter;
import com.TheLa.models.Category;
import com.TheLa.models.Product;
import com.TheLa.services.implement.ProductService;
import com.example.TheLa.R;

import java.util.List;

public class LatestProductFragment extends Fragment {
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    ProductService productService = new ProductService();

    public LatestProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_product, container, false);

        setupRecyclerViews(view);

        setupButtonBack(view);

        return view;
    }

    private void setupButtonBack(View view) {
        ImageView btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void setupRecyclerViews(View view) {
        // Product
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Product> productList = productService.get10RecentActiveAndNotDeletedProducts();

        productAdapter = new ProductAdapter(productList, getContext());
        productRecyclerView.setAdapter(productAdapter);
    }
}