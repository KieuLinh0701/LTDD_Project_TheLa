package com.TheLa.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.TheLa.models.Product;
import com.example.TheLa.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private List<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
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
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());
        if (product.getPrice() != 0) {
            holder.productPrice.setText("$" + product.getPrice());
        } else {
            holder.productPrice.setText("");
        }
        if (!product.isStatus()) {

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

//        Glide.with(holder.itemView.getContext())
//                .load(product.getImage()) // Đường dẫn ảnh
//                .placeholder(R.drawable.placeholder) // Ảnh chờ
//                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, addCart;
        TextView productName, productDescription, productPrice, productStockStatus;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            addCart = itemView.findViewById(R.id.addToCartButton);
        }
    }
}
