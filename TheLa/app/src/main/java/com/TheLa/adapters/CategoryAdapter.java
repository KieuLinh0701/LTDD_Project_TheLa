package com.TheLa.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TheLa.models.CategoryModel;
import com.example.TheLa.R;

import java.io.InputStream;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<CategoryModel> categoryList;
    private Context context;
    private IOnItemClickListener listener;

    public CategoryAdapter(List<CategoryModel> categoryList, Context context, IOnItemClickListener listener) {
        this.categoryList = categoryList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel category = categoryList.get(position);
        holder.categoryName.setText(category.getName());

        //xử lý ảnh
        String categoryImage = category.getImage(); // Tên tài nguyên hình ảnh (không có phần mở rộng)

        // Truy vấn tài nguyên trong `res/raw`
        int resourceId = context.getResources().getIdentifier(categoryImage, "raw", context.getPackageName());

        if (resourceId != 0) {
            // Tải ảnh từ `res/raw`
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Hiển thị trong ImageView
            holder.categoryImage.setImageBitmap(bitmap);
        } else {
            // Hiển thị ảnh mặc định nếu không tìm thấy
            holder.categoryImage.setImageResource(R.drawable.ic_back);
        }

        // **Cập nhật trạng thái giao diện (viền và tick) dựa trên isSelected**
        if (category.isSelected()) {
            holder.categoryImage.setBackgroundResource(R.drawable.circle_border); // Viền xanh
        } else {
            holder.categoryImage.setBackgroundResource(R.drawable.circle_background); // Xóa viền
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public CategoryModel getItem(int position) {
        return categoryList.get(position);
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImage;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryImage = itemView.findViewById(R.id.categoryImage);
        }
    }
}
