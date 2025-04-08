package com.TheLa.fragments.home;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.TheLa.dto.PromotionDto;
import com.example.TheLa.R;

import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PromotionDetailFragment extends Fragment {

    private TextView tvPromotionStatus, tvPromotionName, tvPromotionDes, tvPromotionStartDate, tvPromotionEndDate;
    private ImageView imgPromotion;
    private ImageButton btnClose;
    private PromotionDto dto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion_detail, container, false);

        initializeViews(view);

        retrieveProductFromBundle();

        initializeData();

        setUpDetaiRecyclerView();

        setupEventHandlers();

        return view;
    }

    // Thiết lập các sự kiện
    private void setupEventHandlers() {
        btnClose.setOnClickListener(v -> clickBtnClose());
    }

    private void setUpDetaiRecyclerView() {

    }

    // Xử lý sự kiện khi nút đóng được nhấn
    private void clickBtnClose() {
        requireActivity().onBackPressed();
    }

    private String setUpStatusPromotion(Timestamp startDate, Timestamp endDate, Long quantity, Long quantityUsed) {
        if (startDate.after(new Timestamp(System.currentTimeMillis()))) {
            return "Chuẩn bị diễn ra";
        } else if (startDate.before(new Timestamp(System.currentTimeMillis())) && endDate.before(new Timestamp(System.currentTimeMillis()))){
            if (quantity.equals(quantityUsed)) {
                return "Hết lượt sử dụng";
            } else {
                return "Đang diễn ra";
            }
        } else {
            return "Đã kết thúc";
        }
    }

    private String formattedDate(Timestamp date) {
        // Định dạng ngày tháng năm
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(new Date(date.getTime()));
    }

    private void setUpImage(String image) {
        // Truy vấn tài nguyên trong `res/raw`
        int resourceId = getContext().getResources().getIdentifier(image, "raw", getContext().getPackageName());

        if (resourceId != 0) {
            // Tải ảnh từ `res/raw`
            InputStream inputStream = getContext().getResources().openRawResource(resourceId);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Hiển thị trong ImageView
            imgPromotion.setImageBitmap(bitmap);
        } else {
            // Hiển thị ảnh mặc định nếu không tìm thấy
            imgPromotion.setImageResource(R.drawable.ic_default);
        }
    }

    private void initializeData() {
        tvPromotionStatus.setText(setUpStatusPromotion(dto.getStartDate(), dto.getEndDate(), dto.getQuantity(), dto.getQuantityUsed()));
        tvPromotionName.setText(dto.getName());
        tvPromotionDes.setText(dto.getDescription());

        tvPromotionStartDate.setText(formattedDate(dto.getStartDate()));
        tvPromotionEndDate.setText(formattedDate(dto.getEndDate()));

        setUpImage(dto.getImage());
    }

    // Lấy thông tin sản phẩm từ Bundle
    private void retrieveProductFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            dto = (PromotionDto) bundle.getSerializable("promotion");
        }
    }

    @SuppressLint("WrongViewCast")
    private void initializeViews(View view) {
        tvPromotionStatus = view.findViewById(R.id.tvPromotionStatus);
        tvPromotionName = view.findViewById(R.id.tvPromotionName);
        tvPromotionDes = view.findViewById(R.id.tvPromotionDes);
        tvPromotionStartDate = view.findViewById(R.id.tvPromotionStartDate);
        tvPromotionEndDate = view.findViewById(R.id.tvPromotionEndDate);
        imgPromotion = view.findViewById(R.id.imgPromotion);
        btnClose = view.findViewById(R.id.btnClose);
    }
}