package com.example.registerloginexample;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddRequest extends StringRequest {
    private static final String URL = "http://3.209.169.0/Add.php";
    private Map<String, String> params;

    public AddRequest(int fk_food_custid, int refId, String productName, String quantity, String expiryDate, String imagePath, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("fk_food_custid", String.valueOf(fk_food_custid)); // 수정: fk_food_custid를 전달
        params.put("refId", String.valueOf(refId));
        params.put("productName", productName);
        params.put("quantity", quantity);
        params.put("expiryDate", expiryDate);
        params.put("imagePath", imagePath);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
