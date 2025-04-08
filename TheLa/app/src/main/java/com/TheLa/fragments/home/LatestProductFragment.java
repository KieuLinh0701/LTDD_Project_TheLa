package com.TheLa.fragments.home;

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
import com.TheLa.Api.ProductApi;
import com.TheLa.adapters.ProductAdapter;
import com.TheLa.dto.ProductDto;
import com.TheLa.fragments.ProductDetailFragment;
import com.example.TheLa.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LatestProductFragment extends Fragment {
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<ProductDto> list = new ArrayList<>();

    public LatestProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_product, container, false);

        getLatestProducts();
        initializeViews(view);
        setupBackButton(view);
        loadProductRecyclerView(view);

        return view;
    }

    // Khởi tạo các thành phần giao diện
    private void initializeViews(View view) {
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
    }

    // Thiết lập sự kiện cho nút quay lại
    private void setupBackButton(View view) {
        ImageView btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    // Tải dữ liệu và thiết lập RecyclerView sản phẩm
    private void loadProductRecyclerView(View view) {
        setupProductRecyclerView();
    }

    // Lấy danh sách 10 sản phẩm mới nhất
    private void getLatestProducts() {
        ProductApi productApi = ApiClient.getRetrofitInstance().create(ProductApi.class);
        Call<List<ProductDto>> call = productApi.get10RecentActiveAndNotDeletedProducts();

        call.enqueue(new Callback<List<ProductDto>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<ProductDto>> call, Response<List<ProductDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    list.clear();
                    list.addAll(response.body());
                    productAdapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFragment", "Lỗi");
                }
            }

            @Override
            public void onFailure(Call<List<ProductDto>> call, Throwable t) {
                Log.e("HomeFragment", "Lỗi khi gọi API: " + t.getMessage());
            }
        });
    }

    // Thiết lập RecyclerView cho danh sách sản phẩm
    private void setupProductRecyclerView() {
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        productAdapter = new ProductAdapter(list, getContext(), this::onProductClicked);
        productRecyclerView.setAdapter(productAdapter);
    }

    // Xử lý sự kiện khi một sản phẩm được nhấn
    private void onProductClicked(int position) {
        ProductDto clickedProduct = productAdapter.getItem(position);

        navigateToProductDetailFragment(clickedProduct);
    }

    // Điều hướng đến ProductDetailFragment
    private void navigateToProductDetailFragment(ProductDto product) {
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();

        // Truyền dữ liệu sản phẩm qua Bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", (Serializable) product);
        productDetailFragment.setArguments(bundle);

        // Thay thế fragment hiện tại bằng fragment chi tiết sản phẩm
        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_bottom,  // Hiệu ứng khi Fragment mới xuất hiện
                        android.R.anim.fade_out, // Hiệu ứng khi Fragment cũ biến mất
                        android.R.anim.fade_out, // Hiệu ứng khi quay lại Fragment cũ
                        R.anim.slide_out_bottom  // Hiệu ứng khi Fragment mới biến mất
                )
                .replace(R.id.fragment_home, productDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}