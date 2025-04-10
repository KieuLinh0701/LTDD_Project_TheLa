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
import com.TheLa.Api.CategoryApi;
import com.TheLa.Api.ProductApi;
import com.TheLa.activities.MainActivity;
import com.TheLa.adapters.CategoryAdapter;
import com.TheLa.adapters.ProductAdapter;
import com.TheLa.dto.CategoryDto;
import com.TheLa.dto.ProductDto;
import com.example.TheLa.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreFragment extends Fragment {
    private long categoryId;
    private RecyclerView categoryRecyclerView;
    private RecyclerView productRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private List<CategoryDto> categoryList = new ArrayList<>();
    private List<ProductDto> productList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        // Nhận tham số từ Bundle
        extractArguments();

        // Khởi tạo RecyclerView
        setupRecyclerViews(view);

        GetAllProducts();

        return view;
    }

    public void GetProducts(Long categoryId) {
        ProductApi productApi = ApiClient.getRetrofitInstance().create(ProductApi.class);
        Call<List<ProductDto>> call = productApi.getActiveAndNotDeletedProducts(categoryId);

        CallApi(call);
    }

    public void GetAllProducts() {
        ProductApi productApi = ApiClient.getRetrofitInstance().create(ProductApi.class);
        Call<List<ProductDto>> call = productApi.getAllActiveAndNotDeletedProducts();

        CallApi(call);
    }

    private void CallApi(Call<List<ProductDto>> call) {
        call.enqueue(new Callback<List<ProductDto>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<ProductDto>> call, Response<List<ProductDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
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

    private void extractArguments() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            categoryId = arguments.getLong("categoryId");
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupRecyclerViews(View view) {
        setupProductRecyclerView(view);
        setupCategoryRecyclerView(view);
    }

    private void setupProductRecyclerView(View view) {
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        productAdapter = new ProductAdapter(productList, getContext(), position -> navigateToProductDetail(productList.get(position)));
        productRecyclerView.setAdapter(productAdapter);
    }

    private void setupCategoryRecyclerView(View view) {
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);

        GetCategory();

        categoryAdapter = new CategoryAdapter(categoryList, getContext(), position -> handleCategoryClick(categoryList, position));
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    //Lấy category từ API
    public void GetCategory() {
        CategoryApi api = ApiClient.getRetrofitInstance().create(CategoryApi.class);
        Call<List<CategoryDto>> call = api.getAllActiveAndNotDeletedCategories();

        call.enqueue(new Callback<List<CategoryDto>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<CategoryDto>> call, Response<List<CategoryDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body());

                    highlightSelectedCategory(categoryList);
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFragment", "Lỗi");
                }
            }

            @Override
            public void onFailure(Call<List<CategoryDto>> call, Throwable t) {
                Log.e("HomeFragment", "Lỗi khi gọi API: " + t.getMessage());
            }
        });
    }

    private void highlightSelectedCategory(List<CategoryDto> categoryList) {
        for (CategoryDto category : categoryList) {
            if (category.getCategoryId() == categoryId) {
                category.setSelected(true);
                break;
            }
        }
    }

    private void handleCategoryClick(List<CategoryDto> categoryList, int position) {
        CategoryDto clickedCategory = categoryList.get(position);

        updateCategorySelection(categoryList, clickedCategory);
        if (clickedCategory.isSelected()) {
            GetProducts(clickedCategory.getCategoryId());
        } else {
            GetAllProducts();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateCategorySelection(List<CategoryDto> categoryList, CategoryDto clickedCategory) {
        for (CategoryDto category : categoryList) {
            if (!category.equals(clickedCategory)) {
                category.setSelected(false);
            }
        }

        clickedCategory.setSelected(!clickedCategory.isSelected());
        categoryAdapter.notifyDataSetChanged();
    }

    private void navigateToProductDetail(ProductDto clickedProduct) {
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("product", (Serializable) clickedProduct);
        productDetailFragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_bottom,
                        android.R.anim.fade_out,
                        android.R.anim.fade_out,
                        R.anim.slide_out_bottom
                )
                .replace(R.id.fragment_store, productDetailFragment)
                .addToBackStack(null)
                .commit();

        // Ẩn BottomNavigationView
        ((MainActivity) getActivity()).setBottomNavigationVisibility(false);

        // Lắng nghe sự kiện quay lại để hiển thị lại BottomNavigationView
        getParentFragmentManager().addOnBackStackChangedListener(() -> {
            if (getParentFragmentManager().getBackStackEntryCount() == 0) {
                ((MainActivity) requireActivity()).setBottomNavigationVisibility(true);
            }
        });
    }
}