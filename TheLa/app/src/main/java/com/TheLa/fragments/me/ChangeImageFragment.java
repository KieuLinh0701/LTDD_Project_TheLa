package com.TheLa.fragments.me;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.TheLa.dto.UserDto;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;

import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public class ChangeImageFragment extends Fragment {
    private ImageView ivBack, ivImage;
    private AppCompatButton btnChooseImage, btnSave;
    private UserDto user;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int READ_EXTERNAL_STORAGE_REQUEST = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra quyền truy cập bộ nhớ
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_image, container, false);

        initialViews(view);
        initialData();
        addEvents();

        return view;
    }

    private void initialViews(View view) {
        ivBack = view.findViewById(R.id.ivBack);
        ivImage = view.findViewById(R.id.ivImage);
        btnChooseImage = view.findViewById(R.id.btnChooseImage);
        btnSave = view.findViewById(R.id.btnSave);
    }

    private void initialData() {
        getUser();
        setImage();
    }

    private void addEvents() {
        ivBack.setOnClickListener(v -> ivBackClick());
        btnChooseImage.setOnClickListener(v -> openImageChooser());
        btnSave.setOnClickListener(v -> btnSaveClick());
    }

    private void btnSaveClick() {
        // Logic lưu ảnh (tùy chỉnh theo nhu cầu)
    }

    @SuppressLint("IntentReset")
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData(); // URI của ảnh được chọn
            try {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);

                // Nén ảnh trước khi hiển thị
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4; // Giảm kích thước ảnh (1/4 kích thước gốc)
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);

                // Gán ảnh cho ImageView
                ivImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e("ChangeImageFragment", "Error loading image", e);
            }
        }
    }

    private void ivBackClick() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void getUser() {
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(getContext());
        user = preferenceManager.getUser();
    }



    private void setImage() {
        
    }

}