package com.example.registerloginexample;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddRequest extends StringRequest {

    private static final String BASE_URL = "http://3.209.169.0/Add.php"; // Add.php 파일의 경로
    private Map<String, String> params;
    private File imageFile;

    public AddRequest(
            int fk_food_custid,
            int ref_id,
            String f_name,
            String f_count,
            String end_date,
            File f_image,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener
    ) {
        super(Method.GET, buildUrl(fk_food_custid, ref_id, f_name, f_count, end_date, f_image), listener, errorListener);

        params = new HashMap<>();
    }

    private static String buildUrl(
            int fk_food_custid,
            int ref_id,
            String f_name,
            String f_count,
            String end_date,
            File f_image
    ) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("?");
        urlBuilder.append("fk_food_custid=").append(fk_food_custid);
        urlBuilder.append("&");
        urlBuilder.append("f_name=").append(f_name);
        urlBuilder.append("&");
        urlBuilder.append("end_date=").append(end_date);

        if (ref_id >= 1 && ref_id <= 9) {
            urlBuilder.append("&");
            urlBuilder.append("ref_id=").append(ref_id);
        }

        urlBuilder.append("&");
        urlBuilder.append("f_count=").append(f_count);

        // 이미지를 Base64로 변환하여 쿼리 매개변수로 추가
        urlBuilder.append("&");
        urlBuilder.append("f_image=").append(convertImageFileToBase64(f_image));

        return urlBuilder.toString();
    }

    private static String convertImageFileToBase64(File imageFile) {
        try {
            byte[] bytesArray = new byte[(int) imageFile.length()];
            FileInputStream fis = new FileInputStream(imageFile);
            fis.read(bytesArray);
            fis.close();
            return Base64.encodeToString(bytesArray, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 변환 실패 시 null 반환
        }
    }
}

