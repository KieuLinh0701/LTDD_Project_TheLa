package com.TheLa.fragments.home;

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

        return view;
    }

    private void setupRecyclerViews(View view) {
        // Product
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Product> productList = productService.findActiveAndNotDeletedProductsByCategoryId(categoryId);

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

            //Lọc sản phẩm theo category và cập nhật lại trang
            List<Product> filteredProducts = productService.findActiveAndNotDeletedProductsByCategoryId(clickedCategory.getCategoryId());
            productAdapter.updateData(filteredProducts);
        });

        categoryRecyclerView.setAdapter(categoryAdapter);
    }
}