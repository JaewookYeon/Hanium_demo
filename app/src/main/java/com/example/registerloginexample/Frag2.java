package com.example.registerloginexample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    private Uri photoUri;
    private static final int REQUEST_IMAGE_CAPTURE = 100;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle2 = getArguments();
        if (bundle2 != null) {
            login_id = bundle2.getString("login_id");
            Log.d("Frag2", "Received login_id: " + login_id);
        }

        View view = inflater.inflate(R.layout.frag2, container, false);

        btn_want = view.findViewById(R.id.btn_want);
        btn_add = view.findViewById(R.id.btn_add);

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
                intent.putExtra("login_id", login_id);
                startActivityForResult(intent, ADD_PRODUCT_REQUEST);
            }
        });

        addedProductList = new ArrayList<>();
        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productAdapter = new ProductAdapter(addedProductList, getContext());
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        productRecyclerView.setAdapter(productAdapter);

        // 서버에서 데이터 가져오는 부분을 onCreate에서 onCreateView로 이동
        getProductsFromServer();

        return view;
    }

    static class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
        private final List<ProductItem> productList;
        private final Context context;

        public ProductAdapter(List<ProductItem> productList, Context context) {
            this.productList = productList;
            this.context = context;
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            ProductItem productItem = productList.get(position);

            if (productItem.getImageBitmap() != null) {
                holder.productImageView.setImageBitmap(productItem.getImageBitmap());
            } else {
                holder.productImageView.setImageResource(R.drawable.ic_settings); // 기본 이미지 설정
            }

            holder.refIdTextView.setText("냉장고 번호: " + productItem.getRefId());
            holder.productNameTextView.setText("상품 이름: " + productItem.getProductName());
            holder.quantityTextView.setText("수량: " + productItem.getQuantity());
            holder.expiryDateTextView.setText("유통기한: " + productItem.getExpiryDate());
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        static class ProductViewHolder extends RecyclerView.ViewHolder {
            ImageView productImageView;
            TextView refIdTextView;
            TextView productNameTextView;
            TextView quantityTextView;
            TextView expiryDateTextView;

            public ProductViewHolder(@NonNull View itemView) {
                super(itemView);

                productImageView = itemView.findViewById(R.id.productImageView);
                refIdTextView = itemView.findViewById(R.id.refIdTextView);
                productNameTextView = itemView.findViewById(R.id.productNameTextView);
                quantityTextView = itemView.findViewById(R.id.quantityTextView);
                expiryDateTextView = itemView.findViewById(R.id.expiryDateTextView);
            }
        }
    }

    private void getProductsFromServer() {
        String url = "http://3.209.169.0/get_products.php";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            addedProductList.clear(); // 기존 데이터를 초기화합니다.

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject productObject = response.getJSONObject(i);
                                int refId = productObject.getInt("ref_id");
                                String productName = productObject.getString("f_name");
                                String quantity = productObject.getString("f_count");
                                String expiryDate = productObject.getString("end_date");
                                String imageUrl = productObject.getString("image_url"); // 이미지 URL 가져오기

                                // ImageRequest를 사용하여 이미지를 가져오고 표시
                                ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        ProductItem productItem = new ProductItem(refId, productName, quantity, expiryDate, bitmap);
                                        addedProductList.add(productItem);
                                        productAdapter.notifyDataSetChanged(); // 어댑터를 업데이트합니다.
                                    }
                                }, 0, 0, null, null);

                                // 이미지 요청을 큐에 추가
                                Volley.newRequestQueue(getContext()).add(imageRequest);

                                Log.d("Frag2", "서버 응답: " + response.toString());
                                Log.d("Frag2", "refId: " + refId);
                                Log.d("Frag2", "productName: " + productName);
                                Log.d("Frag2", "quantity: " + quantity);
                                Log.d("Frag2", "expiryDate: " + expiryDate);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONParsingError", "JSON 파싱 오류: " + e.getMessage());
                            Toast.makeText(getContext(), "JSON 파싱 오류", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            String errorMessage = new String(error.networkResponse.data);
                            Log.e("ServerResponseError", "HTTP 상태 코드: " + statusCode);
                            Log.e("ServerResponseError", "오류 메시지: " + errorMessage);
                        } else {
                            Log.e("ServerResponseError", "네트워크 응답 오류 발생");
                        }
                        Toast.makeText(getContext(), "서버 응답 오류", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Volley 요청 큐에 추가
        Volley.newRequestQueue(getContext()).add(request);
    }
}
