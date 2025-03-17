package com.TheLa.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.TheLa.R;

import java.io.InputStream;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<String> imageList; // List of image resource IDs
    private Context context;

    public ImageAdapter(Context context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String productImage = imageList.get(position);

        // Truy vấn tài nguyên trong `res/raw`
        int resourceId = context.getResources().getIdentifier(productImage, "raw", context.getPackageName());

        if (resourceId != 0) {
            // Tải ảnh từ `res/raw`
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Hiển thị trong ImageView
            holder.productImage.setImageBitmap(bitmap);
        } else {
            // Hiển thị ảnh mặc định nếu không tìm thấy
            holder.productImage.setImageResource(R.drawable.ic_back);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.itemImage);
        }
    }
}
