package com.example.registerloginexample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.IOException;

public class Frag2 extends Fragment {

    private ImageView btn_want;
    private ImageView btn_add;

    private TextView pre_view;
    private ImageView productImageView;

    private Uri photoUri;

    private static final int REQUEST_IMAGE_CAPTURE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag2, container, false);

        btn_want = view.findViewById(R.id.btn_want);
        btn_add = view.findViewById(R.id.btn_add);
        pre_view = view.findViewById(R.id.previewTextView);
        productImageView = view.findViewById(R.id.productImageView);

        btn_want.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Want.class);
                startActivity(intent);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Add.class);
                startActivity(intent);
            }
        });

        if (getArguments() != null) {
            String productName = getArguments().getString("productName");
            String quantity = getArguments().getString("quantity");
            String expiryDate = getArguments().getString("expiryDate");

            String previewText = "상품 이름: " + productName + "\n수량: " + quantity + "\n유통기한: " + expiryDate;
            pre_view.setText(previewText);

            // Add.java에서 가져온 이미지 파일을 설정
            if (photoUri != null) {
                try {
                    Bitmap photoBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                    productImageView.setImageBitmap(photoBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return view;
    }

    public void setPhotoUri(Uri uri) {
        photoUri = uri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap photoBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                productImageView.setImageBitmap(photoBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
