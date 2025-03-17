package com.TheLa.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TheLa.adapters.CategoryAdapter;
import com.TheLa.adapters.ProductAdapter;
import com.TheLa.fragments.home.BestSellerFragment;
import com.TheLa.fragments.home.LatestProductFragment;
import com.TheLa.fragments.home.ProductDetailFragment;
import com.TheLa.fragments.home.SearchProductFragment;
import com.TheLa.models.CategoryModel;
import com.TheLa.models.ProductModel;
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

        setCategoryRecyclerView(view);
        setupProductRecyclerView(view);

        setLatestCardView(view);
        setBestSellerCardView(view);

        return view;
    }

    private void setBestSellerCardView(View view) {
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

    private void setLatestCardView(View view) {
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

    private void setupProductRecyclerView(View view) {
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ProductModel> productModelList = productService.getAllActiveAndNotDeletedProducts();

        productAdapter = new ProductAdapter(productModelList, getContext(), position -> {
            ProductModel clickedProduct = productModelList.get(position);

            ProductDetailFragment productDetailFragment = new ProductDetailFragment();

            // Tạo một Bundle để truyền dữ liệu
            Bundle bundle = new Bundle();
            bundle.putSerializable("product", clickedProduct); // Truyền ProductModel qua Bundle (ProductModel cần implements Serializable)
            productDetailFragment.setArguments(bundle);

            // Thay thế Fragment hiện tại bằng Fragment mới với hiệu ứng
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_bottom,  // Hiệu ứng khi Fragment mới xuất hiện
                            android.R.anim.fade_out,   // Hiệu ứng khi Fragment cũ biến mất
                            android.R.anim.fade_out, // Hiệu ứng khi quay lại Fragment cũ
                            R.anim.slide_out_bottom    // Hiệu ứng khi Fragment mới biến mất
                    )
                    .replace(R.id.fragment_home, productDetailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        productRecyclerView.setAdapter(productAdapter);
    }

    private void setCategoryRecyclerView(View view) {
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);

        List<CategoryModel> categoryList = categoryService.getAllActiveAndNotDeletedCategories();

        categoryAdapter = new CategoryAdapter(categoryList, getContext(), position -> {
            CategoryModel clickedCategory = categoryList.get(position);

            SearchProductFragment searchProductFragment = new SearchProductFragment();

            // Tạo một Bundle để truyền dữ liệu
            Bundle bundle = new Bundle();
            bundle.putLong("categoryId", clickedCategory.getCategoryId());

            // Đặt Bundle vào Fragment
            searchProductFragment.setArguments(bundle);

            // Thay thế Fragment hiện tại bằng Fragment mới
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_home, searchProductFragment)
                    .addToBackStack(null)
                    .commit();
        });

        categoryRecyclerView.setAdapter(categoryAdapter);
    }
}