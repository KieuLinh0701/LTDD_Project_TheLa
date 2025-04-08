package com.TheLa.adapters;

import android.annotation.SuppressLint;
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

import com.TheLa.dto.PromotionDto;
import com.example.TheLa.R;

import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder>{
    private List<PromotionDto> list;
    private Context context;
    private IOnItemClickListener listener;

    public PromotionAdapter(List<PromotionDto> list, Context context, IOnItemClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_promotion, parent, false);
        return new PromotionViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PromotionViewHolder holder, int position) {
        PromotionDto dto = list.get(position);

        holder.tvPromotionName.setText(dto.getName());
        holder.tvPromotionDescription.setText(dto.getDescription());

        // Lấy giá trị Timestamp
        Timestamp endDate = dto.getEndDate(); // Trả về Timestamp
        // Định dạng ngày tháng năm
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date(endDate.getTime()));
        // Gán giá trị định dạng vào TextView
        holder.tvPromotionEndDate.setText(formattedDate);

        if (dto.getStartDate().after(new Timestamp(System.currentTimeMillis()))) {
            holder.tvPromotionStatus.setText("Chuẩn bị diễn ra");
        } else if (dto.getStartDate().before(new Timestamp(System.currentTimeMillis())) && dto.getEndDate().before(new Timestamp(System.currentTimeMillis()))){
            if (dto.getQuantity().equals(dto.getQuantityUsed())) {
                holder.tvPromotionStatus.setText("Hết lượt sử dụng");
            } else {
                holder.tvPromotionStatus.setText("Đang diễn ra");
            }
        } else {
            holder.tvPromotionStatus.setText("Đã kết thúc");
        }

        // Truy vấn tài nguyên trong `res/raw`
        int resourceId = context.getResources().getIdentifier(dto.getImage(), "raw", context.getPackageName());

        if (resourceId != 0) {
            // Tải ảnh từ `res/raw`
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Hiển thị trong ImageView
            holder.imgPromotion.setImageBitmap(bitmap);
        } else {
            // Hiển thị ảnh mặc định nếu không tìm thấy
            holder.imgPromotion.setImageResource(R.drawable.ic_default);
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

    public PromotionDto getItem(int position) {
        return list.get(position);
    }


    static class PromotionViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPromotion;
        TextView tvPromotionName, tvPromotionDescription, tvPromotionEndDate, tvPromotionStatus;
        private IOnItemClickListener listener;

        public PromotionViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPromotion = itemView.findViewById(R.id.imgPromotion);
            tvPromotionName = itemView.findViewById(R.id.tvPromotionName);
            tvPromotionDescription = itemView.findViewById(R.id.tvPromotionDescription);
            tvPromotionEndDate = itemView.findViewById(R.id.tvPromotionEndDate);
            tvPromotionStatus = itemView.findViewById(R.id.tvPromotionStatus);
        }
    }
}
