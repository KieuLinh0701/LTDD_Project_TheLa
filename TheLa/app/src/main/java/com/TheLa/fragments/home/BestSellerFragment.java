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

public class BestSellerFragment extends Fragment {
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private ProductService productService;

    public BestSellerFragment() {
        // Required empty public constructor
        productService = new ProductService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_best_seller, container, false);

        initializeViews(view);
        initializeRecyclerView(view);
        setupButtonBack(view);

        return view;
    }

    // Khởi tạo các view từ layout.
    private void initializeViews(View view) {
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
    }

    // Thiết lập RecyclerView để hiển thị danh sách sản phẩm.
    private void initializeRecyclerView(View view) {
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ProductModel> productModelList = getBestSellerProducts();

        productAdapter = new ProductAdapter(productModelList, getContext(), this::onProductClicked);
        productRecyclerView.setAdapter(productAdapter);
    }

    //Lấy danh sách các sản phẩm bán chạy nhất.
    private List<ProductModel> getBestSellerProducts() {
        return productService.getTop10BestSellingActiveAndNotDeletedProducts();
    }

    // Xử lý sự kiện khi một sản phẩm được nhấn.
    private void onProductClicked(int position) {
        ProductModel clickedProduct = productAdapter.getItem(position);
        navigateToProductDetailFragment(clickedProduct);
    }

    //Điều hướng đến `ProductDetailFragment`.
    private void navigateToProductDetailFragment(ProductModel clickedProduct) {
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("product", clickedProduct); // Truyền dữ liệu qua Bundle
        productDetailFragment.setArguments(bundle);

        // Thay thế Fragment hiện tại bằng Fragment mới với hiệu ứng
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

    //Thiết lập sự kiện nút quay lại.
    private void setupButtonBack(View view) {
        ImageView btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }
}