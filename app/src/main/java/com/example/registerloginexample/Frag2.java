package com.example.registerloginexample;

import static android.app.Activity.RESULT_OK;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Frag2 extends Fragment {

    private String login_id;

    private ImageView btn_want;

    private ImageView btn_add;

    private static final int ADD_PRODUCT_REQUEST = 1;

    private List<ProductItem> addedProductList = new ArrayList<>();

    private RecyclerView productRecyclerView;

    private ProductAdapter productAdapter;

    private LinearLayout previewLayout;

    private TextView refIdTextView;

    private TextView productNameTextView;

    private TextView quantityTextView;

    private TextView expiryDateTextView;

    private ImageView productImageView;

    private Uri photoUri;

    private static final int REQUEST_IMAGE_CAPTURE = 100;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        Bundle bundle2 = getArguments();
        if (bundle2 != null) {
            login_id = bundle2.getString("login_id");
            Log.d("Frag2", "Received login_id: " + login_id);
        }

        View view = inflater.inflate(R.layout.frag2, container, false);

        btn_want = view.findViewById(R.id.btn_want);
        btn_add = view.findViewById(R.id.btn_add);
        previewLayout = view.findViewById(R.id.previewLinearLayout);
        refIdTextView = view.findViewById(R.id.refIdTextView);
        productNameTextView = view.findViewById(R.id.productNameTextView);
        quantityTextView = view.findViewById(R.id.quantityTextView);
        expiryDateTextView = view.findViewById(R.id.expiryDateTextView);
        productImageView = view.findViewById(R.id.productImageView);


        btn_want.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Frag3", "Received login_id: " + login_id);
                Intent intent = new Intent(getActivity(), Want.class);
                startActivity(intent);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Frag3", "Received login_id: " + login_id);
                Intent intent = new Intent(getActivity(), Add.class);
                startActivityForResult(intent, ADD_PRODUCT_REQUEST);
            }
        });

        addedProductList = new ArrayList<>();
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productAdapter = new ProductAdapter(addedProductList, getContext());
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        productRecyclerView.setAdapter(productAdapter);

        // 데이터베이스에서 상품 정보를 가져와서 추가
        List<ProductItem> databaseProducts = getProductsFromDatabase();
        addedProductList.addAll(databaseProducts);
        productAdapter.notifyDataSetChanged();

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

    private List<ProductItem> getProductsFromDatabase() {
        List<ProductItem> productList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 데이터베이스 연결 정보 설정 (URL, 사용자 이름, 비밀번호)
            String dbUrl = "jdbc:mysql://3.209.169.0:3306/hanium_api";
            String dbUser = "hanium";
            String dbPassword = "1234";

            // 데이터베이스 연결
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // SQL 쿼리 작성
            String sql = "SELECT * FROM FOOD";
            preparedStatement = connection.prepareStatement(sql);

            // 쿼리 실행 및 결과 가져오기
            resultSet = preparedStatement.executeQuery();

            // 결과 처리
            while (resultSet.next()) {
                int refId = resultSet.getInt("ref_id");
                String productName = resultSet.getString("f_name");
                String quantity = resultSet.getString("f_count");
                String expiryDate = resultSet.getString("end_date");

                // ProductItem 객체 생성
                ProductItem productItem = new ProductItem(refId, productName, quantity, expiryDate, null); // Uri는 여기서 null로 설정

                // productList에 추가
                productList.add(productItem);

                Log.d("MyApp", "쿼리 결과 - refId: " + refId + ", productName: " + productName + ", quantity: " + quantity + ", expiryDate: " + expiryDate);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 연결 및 리소스 해제
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return productList;
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
