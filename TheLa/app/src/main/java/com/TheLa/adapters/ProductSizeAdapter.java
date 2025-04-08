package com.TheLa.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TheLa.dto.ProductSizeDto;
import com.example.TheLa.R;

import java.util.List;

public class ProductSizeAdapter extends RecyclerView.Adapter<ProductSizeAdapter.ProductSizeViewHolder> {
    private List<ProductSizeDto> list;
    private Context context;
    private int selectedPosition = -1; // Lưu vị trí đã chọn

    public ProductSizeAdapter(List<ProductSizeDto> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductSizeAdapter.ProductSizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_size, parent, false);
        return new ProductSizeAdapter.ProductSizeViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductSizeAdapter.ProductSizeViewHolder holder, int position) {
        ProductSizeDto model = list.get(position);
        holder.sizeName.setText(model.getSize().getName());
        holder.price.setText("+ " + model.getPrice().toPlainString());
        holder.sizeDescription.setText(model.getSize().getDescription());

        // Set trạng thái checked cho RadioButton
        holder.radioBtnSelected.setChecked(position == selectedPosition);

        // Lắng nghe sự kiện click
        holder.radioBtnSelected.setOnClickListener(v -> {
            // Cập nhật vị trí đã chọn
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged(); // Refresh toàn bộ danh sách
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ProductSizeDto getItem(int position) {
        return list.get(position);
    }

    static class ProductSizeViewHolder extends RecyclerView.ViewHolder {
        TextView sizeName, price, sizeDescription;
        RadioButton radioBtnSelected;

        public ProductSizeViewHolder(@NonNull View itemView) {
            super(itemView);
            sizeName = itemView.findViewById(R.id.tv_size);
            sizeDescription = itemView.findViewById(R.id.tv_description);
            price = itemView.findViewById(R.id.tv_price);
            radioBtnSelected = itemView.findViewById(R.id.radio_btn_selected);
        }
    }
}
