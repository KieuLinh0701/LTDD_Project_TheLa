package com.TheLa.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.TheLa.models.ProductImageModel;
import com.TheLa.models.ProductModel;
import com.example.TheLa.R;

import java.io.InputStream;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private List<ProductModel> productModelList;
    private Context context;
    private IOnItemClickListener listener;

    public ProductAdapter(List<ProductModel> productModelList, Context context, IOnItemClickListener listener) {
        this.productModelList = productModelList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel productModel = productModelList.get(position);

        holder.productName.setText(productModel.getName());
        holder.productDescription.setText(productModel.getDescription());
        if (!productModel.getStatus()) {

            // Xử lý sự kiện khi click vào nút
            holder.addCart.setOnClickListener(v -> {
                holder.addCart.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_gray));
                holder.addCart.setEnabled(false);
            });

            // Hiển thị chữ "Unavailable" khi di chuột quanh nút
            holder.addCart.setOnHoverListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                    Toast.makeText(context, "Unavailable", Toast.LENGTH_SHORT).show();
                }
                return true;
            });
        }

        //Xử lý khi đã thêm vào giỏ hàng, code sau
//        Toast.makeText(context, "Product added to cart!", Toast.LENGTH_SHORT).show();

        //xử lý ảnh

        String productImage = getMainProductImage(productModel); // Tên tài nguyên hình ảnh (không có phần mở rộng)

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
            holder.productImage.setImageResource(R.drawable.ic_default);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public ProductModel getItem(int position) {
        return productModelList.get(position);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<ProductModel> filteredProductModels) {
        this.productModelList.clear();
        this.productModelList.addAll(filteredProductModels);
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, addCart;
        TextView productName, productDescription;
        private IOnItemClickListener listener;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            addCart = itemView.findViewById(R.id.addToCartButton);
        }
    }

    public String getMainProductImage(ProductModel productModel) {
        for (ProductImageModel imageModel : productModel.getListProductImageModelList()) {
            if (imageModel.getMain()) {
                return imageModel.getImage();
            }
        }
        return null;
    }
}
