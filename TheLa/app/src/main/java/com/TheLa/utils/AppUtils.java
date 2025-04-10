package com.TheLa.utils;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
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

    public static String setEmail(String email) {
        // Find the index of '@' in the email
        int atIndex = email.indexOf('@');

        // If '@' is found and the email is valid
        if (atIndex > 1) {
            // Keep the first character and the last character before '@'
            String hint = email.charAt(0) +
                    email.substring(1, atIndex - 1).replaceAll(".", "*") +
                    email.charAt(atIndex - 1) +
                    email.substring(atIndex);
            return hint;
        } else {
            return email;
        }
    }

    public static void switchToFragment(final Fragment originFragment, final Fragment targetFragment, final int fragmentId) {
        // Nếu đã đăng nhập, chuyển đến fragment mới
        FragmentTransaction transaction = originFragment.getParentFragmentManager().beginTransaction();
        transaction.replace(fragmentId, targetFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
    public static void setBottomNavigationVisibility(Object context, boolean isVisible) {
        View bottomNavigationView = null;
        if (context instanceof Fragment) {
            Fragment fragment = (Fragment) context;
            if (fragment.getActivity() != null) {
                bottomNavigationView = fragment.getActivity().findViewById(R.id.bottomNav);
            }
        } else if (context instanceof Activity) {
            Activity activity = (Activity) context;
            bottomNavigationView = activity.findViewById(R.id.bottomNav);
        }
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }
}
