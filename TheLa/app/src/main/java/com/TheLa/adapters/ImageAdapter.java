package com.TheLa.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TheLa.activities.FullScreenImageActivity;
import com.example.TheLa.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<String> imageList; // List of image resource IDs
    private Context context;
    private int layoutResourceId; // Cho phép thay đổi layout linh hoạt

    public ImageAdapter(Context context, List<String> imageList, int layoutResourceId) {
        this.context = context;
        this.imageList = imageList;
        this.layoutResourceId = layoutResourceId; // Lưu layout được truyền vào
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout từ `layoutResourceId` được truyền vào
        View view = LayoutInflater.from(context).inflate(layoutResourceId, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String image = imageList.get(position);

        // Truy vấn tài nguyên trong `res/raw`
        int resourceId = context.getResources().getIdentifier(image, "raw", context.getPackageName());

        if (resourceId != 0) {
            // Tải ảnh từ `res/raw`
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Hiển thị trong ImageView
            holder.image.setImageBitmap(bitmap);
        } else {
            // Hiển thị ảnh mặc định nếu không tìm thấy
            holder.image.setImageResource(R.drawable.ic_default);
        }

        // Thêm sự kiện nhấp chuột
        holder.image.setOnClickListener(v -> {
            Intent intent = new Intent(context, FullScreenImageActivity.class);
            intent.putStringArrayListExtra("imageList", new ArrayList<>(imageList)); // Truyền danh sách ảnh
            intent.putExtra("currentPosition", position); // Truyền vị trí hiện tại
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return imageList != null ? imageList.size() : 0;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.itemImage);
        }
    }
}
