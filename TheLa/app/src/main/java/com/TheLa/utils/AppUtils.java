package com.TheLa.utils;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.TheLa.activities.LoginActivity;
import com.TheLa.activities.MainActivity;
import com.example.TheLa.R;

public class AppUtils {

    // Hàm kiểm tra trạng thái đăng nhập và thực hiện hành động
    public static void checkLogin(final int fragmentId, final Context context, final Fragment originFragment,
                                  final Fragment targetFragment) {
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(context);

        // Kiểm tra nếu đã đăng nhập
        if (preferenceManager.isLoggedIn()) {

            // Nếu đã đăng nhập, chuyển đến fragment mới
            FragmentTransaction transaction = originFragment.getParentFragmentManager().beginTransaction();
            transaction.replace(fragmentId, targetFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            switchToLoginActivity(context);
        }
    }

    public static void switchToLoginActivity(final Context context) {
        Toast.makeText(context, "Đăng nhập ngay để tiếp tục trải nghiệm thú vị nào!", Toast.LENGTH_SHORT).show();

        // Trì hoãn việc chuyển tới LoginActivity sau 2000ms
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        }, 2000); // Trì hoãn 2000ms (2 giây)
    }

    // Điều khiển trạng thái hiển thị hoặc ẩn BottomNavigation
    public static void setBottomNavigationVisibility(Fragment fragment, boolean isVisible) {
        if (fragment.getActivity() != null) {
            View bottomNavigationView = fragment.getActivity().findViewById(R.id.bottomNav);
            if (bottomNavigationView != null) {
                bottomNavigationView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            }
        }
    }
}
