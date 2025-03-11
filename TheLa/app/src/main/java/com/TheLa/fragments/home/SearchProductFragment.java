package com.TheLa.fragments.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.TheLa.adapters.CategoryAdapter;
import com.TheLa.adapters.ProductAdapter;
import com.TheLa.models.Category;
import com.TheLa.models.Product;
import com.TheLa.services.implement.CategoryService;
import com.TheLa.services.implement.ProductService;
import com.example.TheLa.R;

import java.util.List;

public class SearchProductFragment extends Fragment {
    private long categoryId;
    private RecyclerView categoryRecyclerView;
    private RecyclerView productRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    ProductService productService = new ProductService();
    CategoryService categoryService = new CategoryService();

    public SearchProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_product, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            categoryId = arguments.getLong("categoryId");
        }

        setupRecyclerViews(view);
        btnBackClick(view);

        return view;
    }


    private void btnBackClick(View view) {
        view.findViewById(R.id.btnBack).setOnClickListener(
                v -> requireActivity().getSupportFragmentManager().popBackStack()
        );
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupRecyclerViews(View view) {
        // **1. RecyclerView cho sản phẩm**
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Product> productList = productService.findActiveAndNotDeletedProductsByCategoryId(categoryId);
        productAdapter = new ProductAdapter(productList, getContext());
        productRecyclerView.setAdapter(productAdapter);

        // **2. RecyclerView cho danh mục**
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);

        List<Category> categoryList = categoryService.getAllActiveAndNotDeletedCategories();
        for (Category category : categoryList) {
            if (category.getCategoryId() == categoryId) {
                category.setSelected(true);
                break;
            }
        }
        categoryAdapter = new CategoryAdapter(categoryList, getContext(), position -> {
            Category clickedCategory = categoryList.get(position);

            // Cập nhật trạng thái isSelected cho tất cả danh mục
            for (Category category : categoryList) {
                category.setSelected(false); // Reset trạng thái
            }
            clickedCategory.setSelected(true); // Đánh dấu danh mục được chọn

            // Làm mới danh sách danh mục
            categoryAdapter.notifyDataSetChanged();

            // Lọc danh sách sản phẩm theo danh mục được chọn
            List<Product> filteredProducts = productService.findActiveAndNotDeletedProductsByCategoryId(clickedCategory.getCategoryId());
            productAdapter.updateData(filteredProducts);
        });

        categoryRecyclerView.setAdapter(categoryAdapter);
    }

}