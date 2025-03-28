package com.TheLa.fragments.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.TheLa.adapters.ImageAdapter;
import com.TheLa.adapters.ProductAdapter;
import com.TheLa.adapters.ProductSizeAdapter;
import com.TheLa.models.ProductImageModel;
import com.TheLa.models.ProductModel;
import com.TheLa.models.ProductSizeModel;
import com.TheLa.models.ReviewModel;
import com.TheLa.services.implement.ProductService;
import com.TheLa.services.implement.ProductSizeService;
import com.TheLa.services.implement.ReviewService;
import com.example.TheLa.R;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailFragment extends Fragment {
    List<ReviewModel> listReview = new ArrayList<>();
    private AppCompatButton btnAddCart;
    private RecyclerView rvImage;
    private ImageButton btnClose;
    private ImageView btnMoreReview;
    private ProductModel product = new ProductModel();
    private TextView productName;
    TextView tvDescription;
    RecyclerView rvProductSize;
    private TextView tvContent, tvRating, tvOwner, tvEmpty;
    ImageView icStar;
    private ReviewService reviewService = new ReviewService();
    private ProductSizeAdapter adapter;
    private final ProductSizeService productSizeService = new ProductSizeService();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        initializeViews(view);

        retrieveProductFromBundle();
        setProductName();
        setupImageRecyclerView();
        SetDescription();
        setProductSizeRecyclerView(view);
        setUpReview(view);
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
    }

    private void setUpReview(View view) {
        tvContent = view.findViewById(R.id.tv_review_content);
        tvRating = view.findViewById(R.id.tv_review_rating);
        tvOwner = view.findViewById(R.id.tv_review_owner);
        tvEmpty = view.findViewById(R.id.tv_empty);
        icStar = view.findViewById(R.id.ic_star);

        listReview = reviewService.findReviewByProductId(product.getProductId());

        if (!listReview.isEmpty()) {
            tvRating.setVisibility(View.VISIBLE);
            tvOwner.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.VISIBLE);
            icStar.setVisibility(View.VISIBLE);

            tvContent.setText(listReview.get(0).getContent().trim());
            tvRating.setText(String.valueOf(listReview.get(0).getRating()));
            tvOwner.setText(listReview.get(0).getUser().getName().trim());
        } else {
            tvRating.setVisibility(View.GONE);
            tvOwner.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.GONE);
            icStar.setVisibility(View.GONE);

            tvContent.setText("Hiện chưa có nhận xét nào cho sản phẩm này!");
        }

    }

    private void setProductSizeRecyclerView(View view) {
        rvProductSize = view.findViewById(R.id.recyclerView_product_size);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvProductSize.setLayoutManager(layoutManager);

        List<ProductSizeModel> list = productSizeService.findProductSizeByProduct(product);

        adapter = new ProductSizeAdapter(list, getContext());

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
            product = (ProductModel) bundle.getSerializable("product");
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