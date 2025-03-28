package com.TheLa.adapters;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.TheLa.models.ReviewImageModel;
import com.TheLa.models.ReviewModel;
import com.TheLa.services.implement.ReviewImageService;
import com.example.TheLa.R;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<ReviewModel> list;
    private Context context;

    public ReviewAdapter(List<ReviewModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewAdapter.ReviewViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        ReviewModel model = list.get(position);

        holder.tvName.setText(model.getUser().getName());
        holder.tvContent.setText(model.getContent());
        holder.tvTime.setText(setUpReviewDate(model.getReviewDate()));

        // Đặt giá trị Rating
        float ratingValue = model.getRating();
        holder.ratingBar.setRating(ratingValue);

        // Đặt ảnh avatar
        String image = model.getUser().getImage();
        Drawable drawable = setUpImage(image);
        holder.imgAvatar.setImageDrawable(drawable);

        // Gắn danh sách ảnh đánh giá
        List<String> imageNames = setUpReviewImage(model.getReviewId());

        if (imageNames != null && !imageNames.isEmpty()) {
            // Thiết lập LayoutManager và Adapter cho RecyclerView nếu danh sách ảnh không rỗng
            holder.rvImage.setVisibility(View.VISIBLE); // Hiển thị RecyclerView
            holder.rvImage.setLayoutManager(createHorizontalLayoutManager());

            ImageAdapter adapter = new ImageAdapter(context, imageNames, R.layout.item_review_image);
            holder.rvImage.setAdapter(adapter);
        } else {
            // Ẩn RecyclerView nếu danh sách ảnh rỗng hoặc null
            holder.rvImage.setVisibility(View.GONE);
        }
    }

    // Tạo LayoutManager cho RecyclerView theo hướng ngang
    private LinearLayoutManager createHorizontalLayoutManager() {
        return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }

    private List<String> setUpReviewImage(Long reviewId) {
        ReviewImageService service = new ReviewImageService();
        List<ReviewImageModel> list = service.findReviewImageByReviewId(reviewId);

        List<String> imageNames = new ArrayList<>();
        for (ReviewImageModel x : list) {
            imageNames.add(x.getImage());
        }

        return imageNames;
    }

    private String setUpReviewDate(Timestamp modelTimestamp) {
        long modelTimeMillis = modelTimestamp.getTime(); // Chuyển sang milliseconds
        long currentTimestamp = System.currentTimeMillis(); // Thời gian hiện tại

        // Tính khoảng cách thời gian
        long diffInMillis = currentTimestamp - modelTimeMillis;

        // Chuyển đổi khoảng cách sang chuỗi mô tả
        return getTimeAgo(diffInMillis);
    }

    private String getTimeAgo(long diffInMillis) {
        long seconds = diffInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days / 30; // Giả định 1 tháng = 30 ngày

        if (seconds < 60) {
            return seconds + " giây trước";
        } else if (minutes < 60) {
            return minutes + " phút trước";
        } else if (hours < 24) {
            return hours + " giờ trước";
        } else if (days < 30) {
            return days + " ngày trước";
        } else if (months < 12) {
            return months + " tháng trước";
        } else {
            long years = months / 12;
            return years + " năm trước";
        }
    }

    private Drawable setUpImage(String image) {
        // Kiểm tra nếu image là null hoặc rỗng
        if (image == null || image.isEmpty()) {
            // Trả về ảnh mặc định
            return ContextCompat.getDrawable(context, R.drawable.ic_avatar_default);
        }

        // Truy vấn tài nguyên trong `res/raw`
        int resourceId = context.getResources().getIdentifier(image, "raw", context.getPackageName());

        if (resourceId != 0) {
            // Tải ảnh từ `res/raw`
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return new BitmapDrawable(context.getResources(), bitmap);
        } else {
            // Trả về ảnh mặc định nếu không tìm thấy
            return ContextCompat.getDrawable(context, R.drawable.ic_avatar_default);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ReviewModel getItem(int position) {
        return list.get(position);
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgAvatar;
        RatingBar ratingBar;
        TextView tvName, tvTime, tvContent;
        RecyclerView rvImage;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvContent = itemView.findViewById(R.id.tvContent);
            rvImage = itemView.findViewById(R.id.imageRecyclerView);
        }
    }
}