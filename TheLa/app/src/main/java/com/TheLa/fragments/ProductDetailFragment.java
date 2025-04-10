package com.TheLa.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.TheLa.Api.ApiClient;
import com.TheLa.Api.ProductImageApi;
import com.TheLa.Api.ProductSizeApi;
import com.TheLa.Api.ReviewApi;
import com.TheLa.activities.MainActivity;
import com.TheLa.adapters.ImageAdapter;
import com.TheLa.adapters.ProductSizeAdapter;
import com.TheLa.dto.ProductDto;
import com.TheLa.dto.ProductImageDto;
import com.TheLa.dto.ProductSizeDto;
import com.TheLa.dto.ReviewDto;
import com.example.TheLa.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailFragment extends Fragment {
    private AppCompatButton btnAddCart;
    private RecyclerView rvImage;
    private ImageButton btnClose;
    private ImageView btnMoreReview;
    private ProductDto product = new ProductDto();
    private TextView productName;
    TextView tvDescription;
    RecyclerView rvProductSize;
    private TextView tvContent, tvRating, tvOwner, tvEmpty;
    ImageView icStar;
    private ProductSizeAdapter adapter;
    ReviewDto latestReview;
    List<ProductSizeDto> listProductSize = new ArrayList<>();
    List<ProductImageDto> images = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setBottomNavigationVisibility(false); // Ẩn
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        retrieveProductFromBundle();

        getImages();
        getReview();
        getProductSize();

        initializeViews(view);

        setProductName();
        SetDescription();
        setProductSizeRecyclerView();
        setupEventHandlers();

        return view;
    }

    // Khởi tạo các thành phần giao diện
    @SuppressLint("WrongViewCast")
    private void initializeViews(View view) {
        rvImage = view.findViewById(R.id.imageRecyclerView);
        btnClose = view.findViewById(R.id.btnClose);
        btnMoreReview = view.findViewById(R.id.btnMoreReview);
        btnAddCart = view.findViewById(R.id.btnAddCart);
        productName = view.findViewById(R.id.productName);
        tvDescription = view.findViewById(R.id.tv_des);

        tvContent = view.findViewById(R.id.tv_review_content);
        tvRating = view.findViewById(R.id.tv_review_rating);
        tvOwner = view.findViewById(R.id.tv_review_owner);
        tvEmpty = view.findViewById(R.id.tv_empty);
        icStar = view.findViewById(R.id.ic_star);

        rvProductSize = view.findViewById(R.id.recyclerView_product_size);
    }

    private void getReview() {
        ReviewApi api = ApiClient.getRetrofitInstance().create(ReviewApi.class);
        Call<List<ReviewDto>> call = api.getReviewsByProductId(product.getProductId());

        call.enqueue(new Callback<List<ReviewDto>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<ReviewDto>> call, Response<List<ReviewDto>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Lấy review đầu tiên
                    latestReview = response.body().get(0);
                } else {
                    // Nếu không có review, đảm bảo latestReview là null
                    latestReview = null;
                }

                // Gọi hàm setUpReview() sau khi nhận được kết quả API
                setUpReview();
            }

            @Override
            public void onFailure(Call<List<ReviewDto>> call, Throwable t) {
                Log.e("ProductDetailFragment", "Lỗi khi gọi API: " + t.getMessage());
                // Nếu API thất bại, đặt latestReview thành null
                latestReview = null;
                setUpReview();
            }
        });
    }

    private void getProductSize() {
        ProductSizeApi api = ApiClient.getRetrofitInstance().create(ProductSizeApi.class);
        Call<List<ProductSizeDto>> call = api.getProductSizesByProductId(product.getProductId());

        call.enqueue(new Callback<List<ProductSizeDto>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<ProductSizeDto>> call, Response<List<ProductSizeDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (listProductSize == null) {
                        listProductSize = new ArrayList<>();
                    } else {
                        listProductSize.clear();
                    }

                    listProductSize.addAll(response.body());

                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter = new ProductSizeAdapter(listProductSize, getContext());
                        rvProductSize.setAdapter(adapter);
                    }
                } else {
                    Log.e("ProductDetailFragment", "Lỗi: Không có dữ liệu hợp lệ từ API");
                }
            }

            @Override
            public void onFailure(Call<List<ProductSizeDto>> call, Throwable t) {
                Log.e("ProductDetailFragment", "Lỗi khi gọi API: " + t.getMessage());
            }
        });
    }

    private void getImages() {
        ProductImageApi api = ApiClient.getRetrofitInstance().create(ProductImageApi.class);
        Call<List<ProductImageDto>> call = api.getProductImagesByProductId(product.getProductId());

        call.enqueue(new Callback<List<ProductImageDto>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<ProductImageDto>> call, Response<List<ProductImageDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (images == null) {
                        images = new ArrayList<>();
                    } else {
                        images.clear();
                    }

                    images.addAll(response.body());
                    setupImageRecyclerView();
                } else {
                    Log.e("ProductDetailFragment", "Lỗi: Không có dữ liệu hợp lệ từ API");
                }
            }

            @Override
            public void onFailure(Call<List<ProductImageDto>> call, Throwable t) {
                Log.e("ProductDetailFragment", "Lỗi khi gọi API: " + t.getMessage());
            }
        });
    }

    private void setUpReview() {
        if (latestReview != null) {
            // Nếu có review, hiển thị thông tin review
            tvContent.setText(latestReview.getContent().trim());
            tvRating.setText(String.valueOf(latestReview.getRating()));
            tvOwner.setText(latestReview.getUser().getName().trim());

            // Hiển thị các thành phần liên quan đến review
            tvRating.setVisibility(View.VISIBLE);
            tvOwner.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.VISIBLE);
            icStar.setVisibility(View.VISIBLE);
            btnMoreReview.setVisibility(View.VISIBLE);
        } else {
            // Nếu không có review, hiển thị thông báo không có review
            tvContent.setText("Hiện chưa có nhận xét nào cho sản phẩm này!");

            // Ẩn các thành phần liên quan đến review
            tvRating.setVisibility(View.GONE);
            tvOwner.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.GONE);
            icStar.setVisibility(View.GONE);
            btnMoreReview.setVisibility(View.GONE);
        }
    }

    private void setProductSizeRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvProductSize.setLayoutManager(layoutManager);

        adapter = new ProductSizeAdapter(listProductSize, getContext());

        rvProductSize.setAdapter(adapter);
    }

    // Set up dữ liệu cho Description
    private void SetDescription() {
        tvDescription.setText(product.getDescription());
    }

    // Lấy thông tin sản phẩm từ Bundle
    private void retrieveProductFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            product = (ProductDto) bundle.getSerializable("product");
        }
    }

    // Thiết lập RecyclerView để hiển thị ảnh sản phẩm
    private void setupImageRecyclerView() {
        List<String> imageNames = extractImageNames();

        rvImage.setLayoutManager(createHorizontalLayoutManager());
        attachPagerSnapHelper(rvImage);

        ImageAdapter adapter = new ImageAdapter(getContext(), imageNames, R.layout.item_product_image);
        rvImage.setAdapter(adapter);
    }

    // Thiết lập các sự kiện
    private void setupEventHandlers() {
        btnClose.setOnClickListener(v -> handleCloseButtonClick());
        btnMoreReview.setOnClickListener(v -> DirectReviewFragment());
    }

    private void DirectReviewFragment() {
        // Tạo một instance của ReviewFragment
        ReviewProductFragment reviewFragment = new ReviewProductFragment();

        // Tạo một Bundle để chứa tham số
        Bundle bundle = new Bundle();
        bundle.putLong("productId", product.getProductId()); // Truyền tham số productId

        // Gán bundle cho Fragment
        reviewFragment.setArguments(bundle);

        // Chuyển đổi Fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.productDetailFragment, reviewFragment); // fragment_container là ID của layout chứa Fragment
        transaction.addToBackStack(null); // Thêm vào BackStack để có thể quay lại
        transaction.commit();
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
        List<String> imageNames = new ArrayList<>();

        addMainImagesFirst(images, imageNames);
        addRemainingImages(images, imageNames);

        return imageNames;
    }

    // Thêm ảnh chính (isMain = true) vào danh sách
    private void addMainImagesFirst(List<ProductImageDto> images, List<String> imageNames) {
        for (ProductImageDto image : images) {
            if (image.getMain()) {
                imageNames.add(image.getImage());
            }
        }
    }

    // Thêm các ảnh còn lại vào danh sách
    private void addRemainingImages(List<ProductImageDto> images, List<String> imageNames) {
        for (ProductImageDto image : images) {
            if (!image.getMain()) {
                imageNames.add(image.getImage());
            }
        }
    }
}

