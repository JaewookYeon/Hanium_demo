package com.example.registerloginexample;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddRequest extends StringRequest {
    private static final String URL = "http://ruddk658.dothome.co.kr/Add.php";
    private Map<String, String> params;

    public AddRequest(int fk_custid, String productName, String quantity, String expiryDate, String imagePath,
                      Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);

        params = new HashMap<>();
        params.put("fk_custid", String.valueOf(fk_custid));
        params.put("product_name", productName);
        params.put("quantity", quantity);
        params.put("expiry_date", expiryDate);
        params.put("image_path", imagePath);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
