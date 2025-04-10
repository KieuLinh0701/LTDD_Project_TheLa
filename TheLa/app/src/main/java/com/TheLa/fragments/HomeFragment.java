package com.TheLa.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TheLa.Api.ApiClient;
import com.TheLa.Api.ProductApi;
import com.TheLa.activities.MainActivity;
import com.TheLa.adapters.ImagesViewPager2Adapter;
import com.TheLa.adapters.ProductAdapter;
import com.TheLa.dto.ProductDto;
import com.TheLa.fragments.home.BestSellerFragment;
import com.TheLa.fragments.home.LatestProductFragment;
import com.TheLa.fragments.home.PromotionFragment;
import com.TheLa.models.Images;
import com.TheLa.utils.DepthPageTransformer;
import com.example.TheLa.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private ViewPager2 viewPager2;
    private CircleIndicator3 circleIndicator3;
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<ProductDto> listProduct = new ArrayList<>();

    private List<Images> imagesList;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(viewPager2.getCurrentItem() == imagesList.size() - 1) {
                viewPager2.setCurrentItem(0);
            } else {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // Hiển thị lại BottomNavigationView khi quay lại Fragment chính
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavigationVisibility(true);
        }
        Log.d("HomeFragment", "onResume called");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initialViews(view);

        setImage();
        GetProduct();
        setupProductRecyclerView(view);

        // Thiết lập các CardView
        setupCardView(view, R.id.latest, new LatestProductFragment());
        setupCardView(view, R.id.bestSeller, new BestSellerFragment());
        setupCardView(view, R.id.promotion, new PromotionFragment());
        setupCardViewStore(view, R.id.store, R.id.menu_store);

        return view;
    }

    private void initialViews(View view) {
        viewPager2 = view.findViewById(R.id.viewPager);
        circleIndicator3 = view.findViewById(R.id.circleIndicator);
    }

    private List<Images> getImage() {
        // Danh sách hình ảnh
        List<Images> list = new ArrayList<>();
        list.add(new Images(R.drawable.post1));
        list.add(new Images(R.drawable.post2));
        list.add(new Images(R.drawable.post3));
        return list;
    }

    private void setImage() {
        imagesList = getImage();
        ImagesViewPager2Adapter adapter = new ImagesViewPager2Adapter(imagesList);
        viewPager2.setAdapter(adapter);
        circleIndicator3.setViewPager(viewPager2);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 3000); // 3000 milliseconds = 3 seconds
            }
        });

        viewPager2.setPageTransformer(new DepthPageTransformer());
    }

    private void setupCardViewStore(View view, int cardViewId, int menuItemId) {
        CardView cardView = view.findViewById(cardViewId);

        cardView.setOnClickListener(v -> {
            // Chọn tab trong BottomNavigationView
            ((MainActivity) requireActivity()).getBottomNavigationView().setSelectedItemId(menuItemId);
        });
    }


    private void setupCardView(View view, int cardViewId, Fragment targetFragment) {
        CardView cardView = view.findViewById(cardViewId);

        cardView.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).setBottomNavigationVisibility(false);

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_home, targetFragment);
            transaction.addToBackStack(null);

            // Lắng nghe sự kiện quay lại để hiển thị lại BottomNavigationView
            getParentFragmentManager().addOnBackStackChangedListener(() -> {
                if (getParentFragmentManager().getBackStackEntryCount() == 0) {
                    ((MainActivity) requireActivity()).setBottomNavigationVisibility(true);
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
            ((MainActivity) requireActivity()).setBottomNavigationVisibility(false);

            // Lắng nghe sự kiện quay lại để hiển thị lại BottomNavigationView
            getParentFragmentManager().addOnBackStackChangedListener(() -> {
                if (getParentFragmentManager().getBackStackEntryCount() == 0) {
                    ((MainActivity) requireActivity()).setBottomNavigationVisibility(true);
                }
            });
        });

        productRecyclerView.setAdapter(productAdapter);
    }
}