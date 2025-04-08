package com.TheLa.fragments.order;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.TheLa.Api.ApiClient;
import com.TheLa.Api.OrderApi;
import com.TheLa.adapters.OrderAdapter;
import com.TheLa.dto.OrderDto;
import com.TheLa.dto.UserDto;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchOrderFragment extends Fragment {
    private ImageView btnBack;
    private RecyclerView orderRecyclerView;
    private AppCompatEditText etSearch;
    private TextView tvThongBao;
    private UserDto user;
    private SharedPreferenceManager preferenceManager;
    private List<OrderDto> listOrder;
    private OrderAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_order, container, false);

        preferenceManager = new SharedPreferenceManager(requireContext());
        listOrder = new ArrayList<>();

        getUser();
        initializeViews(view);
        setRecyclerView();
        setupButtonBack();
        setupEtSearch();

        return view;
    }
    private void initializeViews(View view) {
        btnBack = view.findViewById(R.id.btnBack);
        orderRecyclerView = view.findViewById(R.id.orderRecyclerView);
        etSearch = view.findViewById(R.id.etSearch);
        tvThongBao = view.findViewById(R.id.tvThongBao);
    }

    private void setRecyclerView() {
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter(listOrder, getContext(), this::onOrderClicked);
        orderRecyclerView.setAdapter(adapter);
    }

    private void setupEtSearch() {
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                String keyword = etSearch.getText().toString().trim();
                if (!keyword.isEmpty()) {
                    getOrders(keyword); // Gọi hàm tìm kiếm
                }
                return true;
            }
            return false;
        });

    }

    private void getUser() {
        user = preferenceManager.getUser();
    }
    private void getOrders(String word) {
        OrderApi api = ApiClient.getRetrofitInstance().create(OrderApi.class);
        Call<List<OrderDto>> call = api.getOrdersBySearch(user.getUserId(), word);

        call.enqueue(new Callback<List<OrderDto>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listOrder.clear();
                    listOrder.addAll(response.body());
                    if (listOrder.isEmpty()) {
                        tvThongBao.setVisibility(View.VISIBLE);
                        orderRecyclerView.setVisibility(View.GONE);
                        tvThongBao.setText("Không có đơn hàng nào được tìm thấy.\nHãy thử lại với từ khóa khác nhé!");
                    } else {
                        tvThongBao.setVisibility(View.GONE);
                        orderRecyclerView.setVisibility(View.VISIBLE);
                    }
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

    private void setupButtonBack() {
        btnBack.setOnClickListener(v -> {
            setBottomNavigationVisibility(true);
            // Quay lại Fragment trước đó trong Back Stack
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            } else {
                // Nếu không có Fragment nào trong Back Stack, quay lại Activity trước đó
                requireActivity().onBackPressed();
            }
        });
    }

    private void setBottomNavigationVisibility(boolean isVisible) {
        View bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

}