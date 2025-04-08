package com.TheLa.fragments.order;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TheLa.Api.ApiClient;
import com.TheLa.Api.OrderApi;
import com.TheLa.adapters.OrderAdapter;
import com.TheLa.dto.OrderDto;
import com.TheLa.dto.UserDto;
import com.TheLa.utils.Constant;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessingOrderFragment extends Fragment {

    private RecyclerView orderRecyclerView;
    private UserDto user;
    private SharedPreferenceManager preferenceManager;
    private List<OrderDto> listOrder;
    private OrderAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_processing_order, container, false);

        preferenceManager = new SharedPreferenceManager(requireContext());
        listOrder = new ArrayList<>();

        getUser();
        initializeViews(view);
        setRecyclerView();
        getOrders();

        return view;
    }
    private void initializeViews(View view) {
        orderRecyclerView = view.findViewById(R.id.orderRecyclerView);
    }

    private void setRecyclerView() {
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter(listOrder, getContext(), this::onOrderClicked);
        orderRecyclerView.setAdapter(adapter);
    }

    private void getUser() {
        user = preferenceManager.getUser();
    }
    private void getOrders() {
        OrderApi api = ApiClient.getRetrofitInstance().create(OrderApi.class);
        Call<List<OrderDto>> call = api.getOrders(user.getUserId(), Constant.ORDER_PROCESSING);

        call.enqueue(new Callback<List<OrderDto>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listOrder.clear();
                    listOrder.addAll(response.body());

                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("ProcessingOrderFragment", "Lỗi");
                }
            }

            @Override
            public void onFailure(Call<List<OrderDto>> call, Throwable t) {
                Log.e("ProcessingOrderFragment", "Lỗi khi gọi API: " + t.getMessage());
            }
        });
    }

    private void onOrderClicked(int position) {
        OrderDto clicked = adapter.getItem(position);
        navigateToOrderDetailFragment(clicked);
    }

    private void navigateToOrderDetailFragment(OrderDto clickedProduct) {
//        ProductDetailFragment productDetailFragment = new ProductDetailFragment();
//
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("product", (Serializable) clickedProduct); // Truyền dữ liệu qua Bundle
//        productDetailFragment.setArguments(bundle);
//
//        // Thay thế Fragment hiện tại bằng Fragment mới với hiệu ứng
//        getParentFragmentManager()
//                .beginTransaction()
//                .setCustomAnimations(
//                        R.anim.slide_in_bottom,  // Hiệu ứng khi Fragment mới xuất hiện
//                        android.R.anim.fade_out, // Hiệu ứng khi Fragment cũ biến mất
//                        android.R.anim.fade_out, // Hiệu ứng khi quay lại Fragment cũ
//                        R.anim.slide_out_bottom  // Hiệu ứng khi Fragment mới biến mất
//                )
//                .replace(R.id.fragment_home, productDetailFragment)
//                .addToBackStack(null)
//                .commit();
    }
}