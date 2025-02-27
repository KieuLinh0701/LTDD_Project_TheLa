package com.TheLa.fragments;

import android.content.Intent;
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
import com.TheLa.fragments.home.BestSellerFragment;
import com.TheLa.fragments.home.LatestProductFragment;
import com.TheLa.models.Category;
import com.TheLa.models.Product;
import com.TheLa.services.implement.CategoryService;
import com.TheLa.services.implement.ProductService;
import com.example.TheLa.R;

import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView categoryRecyclerView;
    private RecyclerView productRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    ProductService productService = new ProductService();
    CategoryService categoryService = new CategoryService();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setupRecyclerViews(view);

        setupLatestCardView(view);
        setupBestSellerCardView(view);

        return view;
    }

    private void setupBestSellerCardView(View view) {
        CardView bestSellerCardView = view.findViewById(R.id.bestSeller);

        bestSellerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment bestSellerFragment = new BestSellerFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_home, bestSellerFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void setupLatestCardView(View view) {
        CardView latestCardView = view.findViewById(R.id.latest);

        latestCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment latestProductFragment = new LatestProductFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_home, latestProductFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void setupRecyclerViews(View view) {
        // Product
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Product> productList = productService.getAllActiveAndNotDeletedProducts();

        productAdapter = new ProductAdapter(productList, getContext());
        productRecyclerView.setAdapter(productAdapter);

        // Category
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);

        List<Category> categoryList = categoryService.getAllActiveAndNotDeletedCategories();

        // Truyền OnItemClickListener vào Adapter
        categoryAdapter = new CategoryAdapter(categoryList, getContext(), position -> {
            // Xử lý sự kiện click vào một category
            Category clickedCategory = categoryList.get(position);
            Toast.makeText(getContext(), "Clicked: " + clickedCategory.getName(), Toast.LENGTH_SHORT).show();

            // Nếu có logic filter sản phẩm theo category, bạn có thể thêm logic ở đây
//            List<Product> filteredProducts = productService.get(clickedCategory.getId());
//            productAdapter.updateData(filteredProducts);
        });

        categoryRecyclerView.setAdapter(categoryAdapter);
    }
}