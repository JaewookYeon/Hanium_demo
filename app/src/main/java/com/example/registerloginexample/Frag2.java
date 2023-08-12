package com.example.registerloginexample;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Frag2 extends Fragment {

    private ImageView btn_want;
    private ImageView btn_add;

    private static final int ADD_PRODUCT_REQUEST = 1;
    private List<ProductItem> addedProductList= new ArrayList<>();

    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;

    private LinearLayout previewLayout;
    private TextView pre_view;
    private ImageView productImageView;

    private TextView refIdTextView;
    private TextView productNameTextView;
    private TextView quantityTextView;
    private TextView expiryDateTextView;

    private Uri photoUri;

    private static final int REQUEST_IMAGE_CAPTURE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag2, container, false);

        btn_want = view.findViewById(R.id.btn_want);
        btn_add = view.findViewById(R.id.btn_add);
        previewLayout = view.findViewById(R.id.previewLinearLayout);
        pre_view = view.findViewById(R.id.previewTextView);
        productImageView = view.findViewById(R.id.productImageView);
        refIdTextView=view.findViewById(R.id.refIdTextView);
        productNameTextView = view.findViewById(R.id.productNameTextView);
        quantityTextView = view.findViewById(R.id.quantityTextView);
        expiryDateTextView = view.findViewById(R.id.expiryDateTextView);


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
                startActivityForResult(intent, ADD_PRODUCT_REQUEST);
            }
        });

        addedProductList = new ArrayList<>();
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productAdapter = new ProductAdapter(addedProductList,getContext());
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        productRecyclerView.setAdapter(productAdapter);

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PRODUCT_REQUEST && resultCode == RESULT_OK && data != null) {
            Integer refId = data.getIntExtra("refId", 0);
            String productName = data.getStringExtra("productName");
            String quantity = data.getStringExtra("quantity");
            String expiryDate = data.getStringExtra("expiryDate");
            photoUri = data.getParcelableExtra("photoUri");

            // Create a local variable to check for duplicate refId
            boolean isDuplicateRefId = false;
            for (ProductItem item : addedProductList) {
                if (item.getRefId() == refId) {
                    isDuplicateRefId = true;
                    break;
                }
            }

            if (isDuplicateRefId) {
                Toast.makeText(getContext(), "중복된 refId입니다. 다른 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                ProductItem productItem = new ProductItem(refId, productName, quantity, expiryDate, photoUri);
                addedProductList.add(0, productItem); // 새로운 항목을 리스트의 맨 앞에 추가
                productAdapter.notifyItemInserted(0); // 어댑터에 항목 추가 알림
                updatePreview(productItem);
                productRecyclerView.scrollToPosition(0); // 스크롤을 맨 위로 이동
            }
        }
    }




    private void updatePreview(ProductItem productItem) {
        if (addedProductList.isEmpty()) {
            previewLayout.setVisibility(View.GONE);
        } else {
            previewLayout.setVisibility(View.VISIBLE);

            refIdTextView.setText("냉장고 번호: " + productItem.getRefId());
            productNameTextView.setText("상품 이름: " + productItem.getProductName());
            quantityTextView.setText("수량: " + productItem.getQuantity());
            expiryDateTextView.setText("유통기한: " + productItem.getExpiryDate());

            if (productItem.getImageUri() != null) {
                try {
                    Bitmap photoBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), productItem.getImageUri());
                    productImageView.setImageBitmap(photoBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                productImageView.setImageDrawable(null);
            }
        }
    }

}
