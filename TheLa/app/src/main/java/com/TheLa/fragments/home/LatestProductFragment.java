package com.TheLa.fragments.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.TheLa.adapters.ProductAdapter;
import com.TheLa.models.ProductModel;
import com.TheLa.services.implement.ProductService;
import com.example.TheLa.R;

import java.util.List;

public class LatestProductFragment extends Fragment {
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private final ProductService productService = new ProductService();

    public LatestProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_product, container, false);

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
        List<ProductModel> products = fetchLatestProducts();

        setupProductRecyclerView(products);
    }

    // Lấy danh sách 10 sản phẩm mới nhất
    private List<ProductModel> fetchLatestProducts() {
        return productService.get10RecentActiveAndNotDeletedProducts();
    }

    // Thiết lập RecyclerView cho danh sách sản phẩm
    private void setupProductRecyclerView(List<ProductModel> products) {
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        productAdapter = new ProductAdapter(products, getContext(), this::onProductClicked);
        productRecyclerView.setAdapter(productAdapter);
    }

    // Xử lý sự kiện khi một sản phẩm được nhấn
    private void onProductClicked(int position) {
        ProductModel clickedProduct = productAdapter.getItem(position);

        navigateToProductDetailFragment(clickedProduct);
    }

    // Điều hướng đến ProductDetailFragment
    private void navigateToProductDetailFragment(ProductModel product) {
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();

        // Truyền dữ liệu sản phẩm qua Bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
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