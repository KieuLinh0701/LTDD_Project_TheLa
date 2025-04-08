package com.TheLa.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.TheLa.dto.OrderDetailDto;
import com.TheLa.dto.OrderDto;
import com.TheLa.utils.Constant;
import com.example.TheLa.R;

import java.io.InputStream;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    private List<OrderDto> list;
    private Context context;
    private IOnItemClickListener listener;

    public OrderAdapter(List<OrderDto> list, Context context, IOnItemClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderDto dto = list.get(position);

        OrderDetailDto orderDetail = dto.getOrderDetails().get(0);
        holder.tvProductName.setText(orderDetail.getProductSize().getProduct().getName());
        Log.d("OrderAdapter", "đ" + orderDetail.getPrice());

        holder.tvSize.setText(orderDetail.getProductSize().getSize().getName());

        holder.tvQuantity.setText("x " + String.valueOf(orderDetail.getQuantity()));
        holder.tvPrice.setText("đ" + orderDetail.getPrice());
        holder.tvTotalPrice.setText("đ" + dto.getTotalPrice());
        holder.tvStatus.setText(dto.getStatus());
        holder.tvSize.setText(orderDetail.getProductSize().getSize().getName());

        if (dto.getStatus().equals(Constant.ORDER_PENDING)) {
            holder.button.setText("Hủy");
            holder.button.setVisibility(View.VISIBLE);
        } if (dto.getStatus().equals(Constant.ORDER_CANCELLED) || dto.getStatus().equals(Constant.ORDER_PROCESSING)) {
            holder.button.setVisibility(View.GONE);
        } if (dto.getStatus().equals(Constant.ORDER_COMPLETED)) {
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setText("Đánh giá");
        }

        // Truy vấn tài nguyên trong `res/raw`
        int resourceId = context.getResources().getIdentifier(orderDetail.getProductSize().getProduct().getProductMainImage(), "raw", context.getPackageName());

        if (resourceId != 0) {
            // Tải ảnh từ `res/raw`
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Hiển thị trong ImageView
            holder.ivProductImage.setImageBitmap(bitmap);
        } else {
            // Hiển thị ảnh mặc định nếu không tìm thấy
            holder.ivProductImage.setImageResource(R.drawable.ic_default);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public OrderDto getItem(int position) {
        return list.get(position);
    }


    static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvSize, tvQuantity, tvPrice, tvStatus, tvTotalPrice;
        AppCompatButton button;
        private IOnItemClickListener listener;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            button = itemView.findViewById(R.id.button);
        }
    }
}
