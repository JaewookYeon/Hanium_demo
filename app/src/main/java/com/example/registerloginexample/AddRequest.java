package com.example.registerloginexample;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddRequest extends StringRequest {
    private static final String ADD_URL = "http://3.209.169.0/Add.php";
    private Map<String, String> params;
    private File imageFile; // 이미지 파일을 저장하는 변수

    public AddRequest(int fk_food_custid, int refId, String productName, String quantity, String expiryDate, File imageFile,
                      Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ADD_URL, listener, errorListener);
        params = new HashMap<>();
        this.imageFile = imageFile; // 이미지 파일을 설정
        params.put("fk_food_custid", String.valueOf(fk_food_custid));
        params.put("refId", String.valueOf(refId));
        params.put("productName", productName);
        params.put("quantity", quantity);
        params.put("expiryDate", expiryDate);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public byte[] getBody() {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }

        // 이미지 파일을 추가
        if (imageFile != null) {
            String fieldName = "file";
            String fileName = imageFile.getName();

            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            builder.addFormDataPart(fieldName, fileName, imageBody);
        }

        Buffer buffer = new Buffer();
        try {
            builder.build().writeTo(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return buffer.readByteArray();
    }
}
