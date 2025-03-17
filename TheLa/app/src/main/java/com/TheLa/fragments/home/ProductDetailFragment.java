package com.TheLa.fragments.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.TheLa.adapters.ImageAdapter;
import com.TheLa.adapters.ProductDetailViewPagerAdapter;
import com.TheLa.models.ProductImageModel;
import com.TheLa.models.ProductModel;
import com.example.TheLa.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailFragment extends Fragment {
    private RecyclerView rvImage;
    private ImageButton btnClose;
    private ProductModel product = new ProductModel();
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TextView productName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        initializeViews(view);
        retrieveProductFromBundle();
        setupTabLayoutWithViewPager();
        setProductName();
        setupImageRecyclerView();
        setupEventHandlers();

        return view;
    }

    // Khởi tạo các thành phần giao diện
    private void initializeViews(View view) {
        rvImage = view.findViewById(R.id.imageRecyclerView);
        btnClose = view.findViewById(R.id.btnClose);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        productName = view.findViewById(R.id.productName);
    }

    private void setupTabLayoutWithViewPager() {
        // Chuẩn bị dữ liệu sản phẩm (Bundle)
        Bundle productData = new Bundle();
        productData.putSerializable("product", product);

        // Adapter cho ViewPager2
        ProductDetailViewPagerAdapter adapter = new ProductDetailViewPagerAdapter(getChildFragmentManager(), getLifecycle(), productData);
        viewPager.setAdapter(adapter);

        // Liên kết TabLayout và ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Thông tin");
                    break;
                case 1:
                    tab.setText("Đánh giá");
                    break;
                default:
                    tab.setText("Thông tin");
                    break;
            }
        }).attach();
    }

    // Lấy thông tin sản phẩm từ Bundle
    private void retrieveProductFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            product = (ProductModel) bundle.getSerializable("product");
        }
    }

    // Thiết lập RecyclerView để hiển thị ảnh sản phẩm
    private void setupImageRecyclerView() {
        List<String> imageNames = extractImageNames();

        rvImage.setLayoutManager(createHorizontalLayoutManager());
        attachPagerSnapHelper(rvImage);

        ImageAdapter adapter = new ImageAdapter(getContext(), imageNames);
        rvImage.setAdapter(adapter);
    }

    // Thiết lập các sự kiện
    private void setupEventHandlers() {
        btnClose.setOnClickListener(v -> handleCloseButtonClick());
    }

    // Xử lý sự kiện khi nút đóng được nhấn
    private void handleCloseButtonClick() {
        requireActivity().onBackPressed();
    }

    // Tạo LayoutManager cho RecyclerView theo hướng ngang
    private LinearLayoutManager createHorizontalLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
    }

    // Gắn PagerSnapHelper cho RecyclerView để hỗ trợ cuộn từng phần tử
    private void attachPagerSnapHelper(RecyclerView recyclerView) {
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    // Thêm tên của sản phẩm
    private void setProductName() {
        productName.setText(product.getName());
    }


    // Trích xuất tên ảnh từ danh sách hình ảnh của sản phẩm
    @Nullable
    private List<String> extractImageNames() {
        List<ProductImageModel> imageModels = product.getListProductImageModelList();
        List<String> imageNames = new ArrayList<>();

        addMainImagesFirst(imageModels, imageNames);
        addRemainingImages(imageModels, imageNames);

        return imageNames;
    }

    // Thêm ảnh chính (isMain = true) vào danh sách
    private void addMainImagesFirst(List<ProductImageModel> imageModels, List<String> imageNames) {
        for (ProductImageModel image : imageModels) {
            if (image.getMain()) {
                imageNames.add(image.getImage());
            }
        }
    }

    // Thêm các ảnh còn lại vào danh sách
    private void addRemainingImages(List<ProductImageModel> imageModels, List<String> imageNames) {
        for (ProductImageModel image : imageModels) {
            if (!image.getMain()) {
                imageNames.add(image.getImage());
            }
        }
    }
}