package com.TheLa.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TheLa.adapters.ShopFragmentAdapter;
import com.TheLa.models.Product;
import com.TheLa.services.implement.ProductService;
import com.example.TheLa.R;

import java.util.List;

public class ShopFragment extends Fragment {
    private RecyclerView recyclerView;
    private ShopFragmentAdapter adapter;
    ProductService productService = new ProductService();

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Product> productList = productService.getAllActiveAndNotDeletedProducts();

        adapter = new ShopFragmentAdapter(productList, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}