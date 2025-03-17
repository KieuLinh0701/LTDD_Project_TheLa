package com.TheLa.fragments.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TheLa.adapters.CategoryAdapter;
import com.TheLa.adapters.ProductAdapter;
import com.TheLa.models.CategoryModel;
import com.TheLa.models.ProductModel;
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
    private ProductService productService = new ProductService();
    private CategoryService categoryService = new CategoryService();

    public SearchProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_product, container, false);

        // Nhận tham số từ Bundle
        extractArguments();

        // Khởi tạo RecyclerView
        setupRecyclerViews(view);

        // Xử lý sự kiện nút quay lại
        setupBackButton(view);

        return view;
    }

    private void extractArguments() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            categoryId = arguments.getLong("categoryId");
        }
    }

    private void setupBackButton(View view) {
        view.findViewById(R.id.btnBack).setOnClickListener(
                v -> requireActivity().getSupportFragmentManager().popBackStack()
        );
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupRecyclerViews(View view) {
        setupProductRecyclerView(view);
        setupCategoryRecyclerView(view);
    }

    private void setupProductRecyclerView(View view) {
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ProductModel> productModelList = productService.findActiveAndNotDeletedProductsByCategoryId(categoryId);

        productAdapter = new ProductAdapter(productModelList, getContext(), position -> navigateToProductDetail(productModelList.get(position)));
        productRecyclerView.setAdapter(productAdapter);
    }

    private void setupCategoryRecyclerView(View view) {
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);

        List<CategoryModel> categoryList = categoryService.getAllActiveAndNotDeletedCategories();
        highlightSelectedCategory(categoryList);

        categoryAdapter = new CategoryAdapter(categoryList, getContext(), position -> handleCategoryClick(categoryList, position));
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void highlightSelectedCategory(List<CategoryModel> categoryList) {
        for (CategoryModel category : categoryList) {
            if (category.getCategoryId() == categoryId) {
                category.setSelected(true);
                break;
            }
        }
    }

    private void handleCategoryClick(List<CategoryModel> categoryList, int position) {
        CategoryModel clickedCategory = categoryList.get(position);

        updateCategorySelection(categoryList, clickedCategory);

        List<ProductModel> filteredProductModels = productService.findActiveAndNotDeletedProductsByCategoryId(clickedCategory.getCategoryId());
        productAdapter.updateData(filteredProductModels);
    }

    private void updateCategorySelection(List<CategoryModel> categoryList, CategoryModel clickedCategory) {
        for (CategoryModel category : categoryList) {
            category.setSelected(false);
        }
        clickedCategory.setSelected(true);
        categoryAdapter.notifyDataSetChanged();
    }

    private void navigateToProductDetail(ProductModel clickedProduct) {
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("product", clickedProduct);
        productDetailFragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_bottom,
                        android.R.anim.fade_out,
                        android.R.anim.fade_out,
                        R.anim.slide_out_bottom
                )
                .replace(R.id.fragment_home, productDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}