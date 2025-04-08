package com.TheLa.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TheLa.Api.ApiClient;
import com.TheLa.Api.ProductApi;
import com.TheLa.adapters.ProductAdapter;
import com.TheLa.dto.ProductDto;
import com.TheLa.fragments.home.BestSellerFragment;
import com.TheLa.fragments.home.LatestProductFragment;
import com.TheLa.fragments.home.PromotionFragment;
import com.example.TheLa.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;

    private List<ProductDto> listProduct = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        GetProduct();
        setupProductRecyclerView(view);

        // Thiết lập các CardView
        setupCardView(view, R.id.latest, new LatestProductFragment());
        setupCardView(view, R.id.bestSeller, new BestSellerFragment());
        setupCardView(view, R.id.promotion, new PromotionFragment());
        setupCardView(view, R.id.store, new StoreFragment());

        return view;
    }

    private void setupCardView(View view, int cardViewId, Fragment targetFragment) {
        CardView cardView = view.findViewById(cardViewId);

        cardView.setOnClickListener(v -> {
            // Ẩn BottomNavigationView
            View bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
            if (bottomNavigationView != null) {
                bottomNavigationView.setVisibility(View.GONE);
            }

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_home, targetFragment);
            transaction.addToBackStack(null);

            // Lắng nghe sự kiện quay lại để hiển thị lại BottomNavigationView
            getParentFragmentManager().addOnBackStackChangedListener(() -> {
                if (getParentFragmentManager().getBackStackEntryCount() == 0) {
                    if (bottomNavigationView != null) {
                        bottomNavigationView.setVisibility(View.VISIBLE);
                    }
                }
            });

            transaction.commit();
        });
    }

    private void setupProductRecyclerView(View view) {
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setProductApdapter(listProduct);
    }

    public void GetProduct() {
        ProductApi productApi = ApiClient.getRetrofitInstance().create(ProductApi.class);
        Call<List<ProductDto>> call = productApi.getAllActiveAndNotDeletedProducts();

        call.enqueue(new Callback<List<ProductDto>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<ProductDto>> call, Response<List<ProductDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listProduct.clear();
                    listProduct.addAll(response.body());
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

    private void setProductApdapter(List<ProductDto> list) {
        productAdapter = new ProductAdapter(list, getContext(), position -> {
            ProductDto clickedProduct = list.get(position);

            ProductDetailFragment productDetailFragment = new ProductDetailFragment();

            // Tạo một Bundle để truyền dữ liệu
            Bundle bundle = new Bundle();
            bundle.putSerializable("product", (Serializable) clickedProduct);
            productDetailFragment.setArguments(bundle);

            // Thay thế Fragment hiện tại bằng Fragment mới với hiệu ứng
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

            // Ẩn BottomNavigationView
            setBottomNavigationVisibility(false);

            // Lắng nghe sự kiện quay lại để hiển thị lại BottomNavigationView
            getParentFragmentManager().addOnBackStackChangedListener(() -> {
                if (getParentFragmentManager().getBackStackEntryCount() == 0) {
                    setBottomNavigationVisibility(true);
                }
            });
        });

        productRecyclerView.setAdapter(productAdapter);
    }
    private void setBottomNavigationVisibility(boolean isVisible) {
        View bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }
}