package com.TheLa.fragments.home.ProductDetail;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TheLa.adapters.CategoryAdapter;
import com.TheLa.adapters.ProductSizeAdapter;
import com.TheLa.models.ProductModel;
import com.TheLa.models.ProductSizeModel;
import com.TheLa.services.implement.ProductSizeService;
import com.TheLa.services.implement.SizeService;
import com.example.TheLa.R;

import java.util.List;

public class InformationProductDetailFragment extends Fragment {
    private ProductModel product;
    TextView tvDescription;
    RecyclerView recyclerView;
    private ProductSizeAdapter adapter;
    private final ProductSizeService productSizeService = new ProductSizeService();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getProductFromBundle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_product_detail, container, false);

        initializeViews(view);
        SetDescription();
        setProductSizeRecyclerView(view);

        return view;
    }

    private void initializeViews(View view) {
        tvDescription = view.findViewById(R.id.tv_des);
    }

    private void setProductSizeRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView_product_size);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        List<ProductSizeModel> list = productSizeService.findProductSizeByProduct(product);

        adapter = new ProductSizeAdapter(list, getContext());

        recyclerView.setAdapter(adapter);
    }

    // Set up dữ liệu cho Description
    private void SetDescription() {
        tvDescription.setText(product.getDescription());
    }

    private void getProductFromBundle() {
        if (getArguments() != null) {
            product = (ProductModel) getArguments().getSerializable("product");
        } else {
            Log.e("InformationFragment", "Arguments are null!");
        }
    }

}